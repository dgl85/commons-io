package org.dgl.commons.io.tabular;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class FileReader {

    private final int MAX_BUFFER_SIZE = Integer.MAX_VALUE;
    private final int headerLength;
    private final int numberOfLines;
    private final int bytesPerLine;
    private final DataLineStructure lineStructure;
    private final FileChannel fileChannel;
    private final ByteBuffer lineBuffer;
    private final String filePath;
    private ByteBuffer multipleLinesBuffer = null;

    public FileReader(String filePath) throws IOException {
        this.filePath = filePath;
        fileChannel = new RandomAccessFile(filePath,"r").getChannel();
        lineStructure = getLineStructureFromFile();
        bytesPerLine = lineStructure.getSizeInBytes();
        if (bytesPerLine > MAX_BUFFER_SIZE) {
            throw new IllegalStateException();
        }
        headerLength = lineStructure.getNumberOfElements()+1;
        long fileSize = fileChannel.size();
        if ((fileSize - headerLength) % bytesPerLine != 0) {
            close();
            throw new IOException();
        }
        numberOfLines = (int) (fileSize - headerLength) / bytesPerLine;
        lineBuffer = ByteBuffer.allocateDirect(bytesPerLine).order(ByteOrder.BIG_ENDIAN);
    }

    public DataLine getLine(int lineIndex) throws IOException {
        validateGet(lineIndex);
        readAndFlip(lineBuffer, getLinePosition(lineIndex));
        return getDataLines(lineBuffer)[0];
    }

    /**
     * Buffer allocation overhead must be taken into account when calling this method
     *
     * @param firstIndex inclusive
     * @param lastIndex exclusive
     * @return
     * @throws IOException
     */
    public DataLine[] getLines(int firstIndex, int lastIndex) throws IOException {
        validateGet(firstIndex, lastIndex);
        long bytesToRead = (lastIndex-firstIndex)*bytesPerLine;
        if (bytesToRead > MAX_BUFFER_SIZE) {
            DataLine[] firstHalf = getLines(firstIndex, firstIndex+((lastIndex-firstIndex)/2));
            DataLine[] secondHalf = getLines(firstIndex+((lastIndex-firstIndex)/2), lastIndex);
            return Utils.mergeArrays(firstHalf, secondHalf);
        }
        if (multipleLinesBuffer == null || multipleLinesBuffer.capacity() < bytesToRead) {
            multipleLinesBuffer = ByteBuffer.allocateDirect((int) bytesToRead);
        }
        return getDataLines(readAndFlip(multipleLinesBuffer, (int) bytesToRead, getLinePosition(firstIndex)));
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public DataLineStructure getLineStructure() {
        return lineStructure;
    }

    public void close() {
        try {
            fileChannel.close();
        } catch (IOException e) {}
    }

    public boolean isOpen() {
        return fileChannel.isOpen();
    }

    public String getFilePath() {
        return filePath;
    }

    private DataLine[] getDataLines(ByteBuffer data) {
        if (data.limit() % lineStructure.getSizeInBytes() != 0) {
            throw new IllegalStateException();
        }
        DataLine[] dataLines = new DataLine[data.limit()/lineStructure.getSizeInBytes()];
        for (int i = 0; i < dataLines.length; i++) {
            DataLine dataLine = new DataLine(lineStructure);
            for (int j = 0; j < lineStructure.getNumberOfElements(); j++) {
                switch (lineStructure.getElementType(j)) {
                    case PrimitiveType.BYTE:
                        dataLine.setByte(j, data.get());
                        break;
                    case PrimitiveType.CHAR:
                        dataLine.setChar(j, data.getChar());
                        break;
                    case PrimitiveType.SHORT:
                        dataLine.setShort(j, data.getShort());
                        break;
                    case PrimitiveType.INT:
                        dataLine.setInt(j, data.getInt());
                        break;
                    case PrimitiveType.LONG:
                        dataLine.setLong(j,data.getLong());
                        break;
                    case PrimitiveType.FLOAT:
                        dataLine.setFloat(j,data.getFloat());
                        break;
                    case PrimitiveType.DOUBLE:
                        dataLine.setDouble(j,data.getDouble());
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
            dataLines[i] = dataLine;
        }
        return dataLines;
    }

    private void validateGet(int lineIndex) throws IOException {
        if (lineIndex < 0 || lineIndex > numberOfLines) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen()) {
            throw new IOException();
        }
    }

    private void validateGet(int firstIndex, int lastIndex) throws IOException {
        if (lastIndex <= firstIndex) {
            throw new IllegalArgumentException();
        }
        if (firstIndex < 0 || lastIndex > numberOfLines) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen()) {
            throw new IOException();
        }
    }

    private long getLinePosition(int lineIndex) {
        return (lineIndex*bytesPerLine)+headerLength;
    }

    private DataLineStructure getLineStructureFromFile() throws IOException {
        ArrayList<Byte> header = new ArrayList<Byte>();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        boolean done = false;
        while (!done) {
            readAndFlip(byteBuffer, header.size());
            byte readByte = byteBuffer.get();
            if  (readByte == 0) {
                done = true;
            } else {
                header.add(readByte);
            }
        }
        DataLineStructure lineStructure = new DataLineStructure(header.size());
        for (int i = 0; i < header.size(); i++) {
            lineStructure.setElementType(i, header.get(i));
        }
        return lineStructure;
    }

    private ByteBuffer readAndFlip(ByteBuffer buffer, long filePosition) throws IOException {
        return readAndFlip(buffer, buffer.capacity(), filePosition);
    }

    /**
     * Buffer might be sliced, so returned buffer must be used, specially if bytesToRead != buffer.capacity()
     * @param buffer
     * @param bytesToRead
     * @param filePosition
     * @return buffer with data
     * @throws IOException
     */
    private ByteBuffer readAndFlip(ByteBuffer buffer, int bytesToRead, long filePosition) throws IOException {
        if (filePosition+bytesToRead > fileChannel.size() || bytesToRead > buffer.capacity()) {
            throw new IOException();
        }
        ByteBuffer finalBuffer = buffer;
        if (bytesToRead != buffer.capacity()) {
            buffer.position(buffer.capacity()-bytesToRead);
            finalBuffer = buffer.slice();
        }
        finalBuffer.clear();
        int totalBytesRead = fileChannel.read(finalBuffer,filePosition);
        while (totalBytesRead != bytesToRead) {
            totalBytesRead += fileChannel.read(finalBuffer,filePosition+totalBytesRead);
        }
        finalBuffer.flip();
        return finalBuffer;
    }

}
