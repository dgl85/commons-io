package org.dgl.commons.io.tabular;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import static org.dgl.commons.io.Definitions.DEFAULT_ENDIANNESS;

public class TabularFileWriter {

    private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE / 10;
    private final int bytesPerLine;
    private final int headerLength;
    private final DataLineStructure lineStructure;
    private final ByteBuffer lineBuffer;
    private final FileChannel fileChannel;
    private final RandomAccessFile randomAccessFile;
    private final String filePath;
    private ByteBuffer multipleLinesBuffer = null;
    private int currentLineIndex;

    public TabularFileWriter(String filePath, DataLineStructure lineStructure, ByteOrder endianness)
            throws IOException {
        this.filePath = filePath;
        this.lineStructure = lineStructure;
        bytesPerLine = lineStructure.getSizeInBytes();
        if (bytesPerLine > MAX_BUFFER_SIZE) {
            throw new IllegalStateException();
        }
        headerLength = lineStructure.getNumberOfElements() + 1;
        boolean newFile = true;
        if (new File(filePath).exists()) {
            try {
                verifyFileHeader(filePath);
            } catch (IllegalStateException | InvalidDataLineStructureException e) {
                close();
                throw e;
            }
            newFile = false;
        }
        randomAccessFile = new RandomAccessFile(filePath, "rw");
        fileChannel = randomAccessFile.getChannel();
        if (newFile) {
            writeFileHeader();
        }
        lineBuffer = ByteBuffer.allocateDirect(lineStructure.getSizeInBytes()).order(endianness);
        currentLineIndex = (int) (fileChannel.size() - headerLength) / bytesPerLine;
    }

    public TabularFileWriter(String filePath, DataLineStructure lineStructure) throws IOException {
        this(filePath, lineStructure, DEFAULT_ENDIANNESS);
    }

    public void writeLine(DataLine dataLine) throws IOException {
        writeLines(currentLineIndex, new DataLine[]{dataLine});
    }

    public void writeLine(int lineIndex, DataLine dataLine) throws IOException {
        writeLines(lineIndex, new DataLine[]{dataLine});
    }

    public void writeLines(DataLine[] dataLines) throws IOException {
        writeLines(currentLineIndex, dataLines);
    }

    public void writeLines(int startLineIndex, DataLine[] dataLines) throws IOException {
        if (startLineIndex < 0 || startLineIndex > currentLineIndex) {
            throw new IndexOutOfBoundsException();
        }
        ByteBuffer writeBuffer;
        if (dataLines.length > 1) {
            long bytesToWrite = (long) dataLines.length * (long) bytesPerLine;
            if (bytesToWrite > MAX_BUFFER_SIZE) {
                DataLine[][] halfs = Utils.splitArrayInHalfs(dataLines);
                writeLines(startLineIndex, halfs[0]);
                writeLines(startLineIndex + halfs[0].length, halfs[1]);
                return;
            }
            if (multipleLinesBuffer == null || multipleLinesBuffer.capacity() < bytesToWrite) {
                multipleLinesBuffer = ByteBuffer.allocateDirect((int) bytesToWrite);
            }
            writeBuffer = multipleLinesBuffer;
        } else {
            writeBuffer = lineBuffer;
        }
        writeBuffer.clear();
        writeDataLinesToBuffer(dataLines, writeBuffer);
        flipAndWrite(writeBuffer, getLinePosition(startLineIndex));
        if (startLineIndex + dataLines.length > currentLineIndex) {
            currentLineIndex += dataLines.length + startLineIndex - currentLineIndex;
        }
    }

    public boolean isOpen() {
        return fileChannel.isOpen();
    }

    public void close() {
        try {
            randomAccessFile.close();
        } catch (IOException e) {}
    }

    public String getFilePath() {
        return filePath;
    }

    private long getLinePosition(int lineIndex) {
        return ((long) lineIndex * (long) bytesPerLine) + (long) headerLength;
    }

    private ByteBuffer writeDataLinesToBuffer(DataLine[] dataLines, ByteBuffer writeBuffer) {
        writeBuffer.clear();
        for (int i = 0; i < dataLines.length; i++) {
            if (!Utils.compareDataLineStructures(dataLines[i].getLineStructure(), lineStructure)) {
                throw new InvalidDataLineStructureException();
            }
            appendDataLineToBuffer(dataLines[i], writeBuffer);
        }
        return writeBuffer;
    }

    private ByteBuffer appendDataLineToBuffer(DataLine dataLine, ByteBuffer writeBuffer) {
        for (int i = 0; i < lineStructure.getNumberOfElements(); i++) {
            switch (lineStructure.getElementType(i)) {
                case PrimitiveType.BYTE:
                    writeBuffer.put(dataLine.getByte(i));
                    break;
                case PrimitiveType.CHAR:
                    writeBuffer.putChar(dataLine.getChar(i));
                    break;
                case PrimitiveType.SHORT:
                    writeBuffer.putShort(dataLine.getShort(i));
                    break;
                case PrimitiveType.INT:
                    writeBuffer.putInt(dataLine.getInt(i));
                    break;
                case PrimitiveType.LONG:
                    writeBuffer.putLong(dataLine.getLong(i));
                    break;
                case PrimitiveType.FLOAT:
                    writeBuffer.putFloat(dataLine.getFloat(i));
                    break;
                case PrimitiveType.DOUBLE:
                    writeBuffer.putDouble(dataLine.getDouble(i));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        return writeBuffer;
    }

    private void verifyFileHeader(String filePath) throws IOException {
        TabularFileReader reader = new TabularFileReader(filePath);
        DataLineStructure fileLineStructure = reader.getLineStructure();
        reader.close();
        if (!Utils.compareDataLineStructures(lineStructure, fileLineStructure)) {
            throw new InvalidDataLineStructureException();
        }
    }

    private void writeFileHeader() throws IOException {
        ByteBuffer headerBuffer = ByteBuffer.allocate(lineStructure.getNumberOfElements() + 1);
        headerBuffer.clear();
        for (int i = 0; i < lineStructure.getNumberOfElements(); i++) {
            headerBuffer.put(lineStructure.getElementType(i));
        }
        headerBuffer.put((byte) 0);
        flipAndWrite(headerBuffer, 0);
    }

    private void flipAndWrite(ByteBuffer buffer, long position) throws IOException {
        buffer.flip();
        while (buffer.hasRemaining()) {
            fileChannel.write(buffer, position);
        }
    }
}
