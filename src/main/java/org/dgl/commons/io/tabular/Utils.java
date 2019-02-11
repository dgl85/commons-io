package org.dgl.commons.io.tabular;

import org.dgl.commons.io.PrimitiveBytes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static DataLine getDataLineFromBytes(byte[] dataLineBytes, DataLineStructure lineStructure) {
        DataLine dataLine = new DataLine(lineStructure);
        int currentOffset = 0;

        for (int i = 0; i < dataLine.getNumberOfElements(); i++) {
            byte elementType = lineStructure.getElementType(i);
            switch (elementType) {
                case PrimitiveType.BYTE:
                    dataLine.setByte(i, dataLineBytes[currentOffset]);
                    break;
                case PrimitiveType.CHAR:
                    dataLine.setChar(i, PrimitiveBytes.getChar(dataLineBytes, currentOffset));
                    break;
                case PrimitiveType.SHORT:
                    dataLine.setShort(i, PrimitiveBytes.getShort(dataLineBytes, currentOffset));
                    break;
                case PrimitiveType.INT:
                    dataLine.setInt(i, PrimitiveBytes.getInt(dataLineBytes, currentOffset));
                    break;
                case PrimitiveType.LONG:
                    dataLine.setLong(i, PrimitiveBytes.getLong(dataLineBytes, currentOffset));
                    break;
                case PrimitiveType.FLOAT:
                    dataLine.setFloat(i, PrimitiveBytes.getFloat(dataLineBytes, currentOffset));
                    break;
                case PrimitiveType.DOUBLE:
                    dataLine.setDouble(i, PrimitiveBytes.getDouble(dataLineBytes, currentOffset));
                    break;
                default:
                    throw new IllegalStateException();
            }
            currentOffset += PrimitiveType.getSizeInBytesForType(elementType);
        }
        return dataLine;
    }

    public static byte[] getDataLineBytes(DataLine dataLine) {
        DataLineStructure lineStructure = dataLine.getLineStructure();
        byte[] bytes = new byte[lineStructure.getSizeInBytes()];
        int currentOffset = 0;

        for (int i = 0; i < dataLine.getNumberOfElements(); i++) {
            byte elementType = lineStructure.getElementType(i);
            switch (elementType) {
                case PrimitiveType.BYTE:
                    bytes[currentOffset] = dataLine.getByte(i);
                    break;
                case PrimitiveType.CHAR:
                    PrimitiveBytes.putCharBytesInArray(dataLine.getChar(i), bytes, currentOffset);
                    break;
                case PrimitiveType.SHORT:
                    PrimitiveBytes.putShortBytesInArray(dataLine.getShort(i), bytes, currentOffset);
                    break;
                case PrimitiveType.INT:
                    PrimitiveBytes.putIntBytesInArray(dataLine.getInt(i), bytes, currentOffset);
                    break;
                case PrimitiveType.LONG:
                    PrimitiveBytes.putLongBytesInArray(dataLine.getLong(i), bytes, currentOffset);
                    break;
                case PrimitiveType.FLOAT:
                    PrimitiveBytes.putFloatBytesInArray(dataLine.getFloat(i), bytes, currentOffset);
                    break;
                case PrimitiveType.DOUBLE:
                    PrimitiveBytes.putDoubleBytesInArray(dataLine.getDouble(i), bytes, currentOffset);
                    break;
                default:
                    throw new IllegalStateException();
            }
            currentOffset += PrimitiveType.getSizeInBytesForType(elementType);
        }
        return bytes;
    }

    /**
     * @param structure1
     * @param structure2
     * @return true if equal, false otherwise
     */
    public static boolean compareDataLineStructures(DataLineStructure structure1, DataLineStructure structure2) {
        boolean structuresAreEqual = true;
        if (structure1.getNumberOfElements() != structure2.getNumberOfElements()
                || structure1.getSizeInBytes() != structure2.getSizeInBytes()) {
            structuresAreEqual = false;
        } else {
            for (int i = 0; i < structure1.getNumberOfElements(); i++) {
                if (structure1.getElementType(i) != structure2.getElementType(i)) {
                    structuresAreEqual = false;
                    break;
                }
            }
        }
        return structuresAreEqual;
    }

    /**
     * @param line1
     * @param line2
     * @return true if equal, false otherwise
     */
    public static boolean compareDataLines(DataLine line1, DataLine line2) {
        if (!compareDataLineStructures(line1.getLineStructure(), line2.getLineStructure())) {
            return false;
        }
        DataLineStructure structure = line1.getLineStructure();
        for (int i = 0; i < line1.getNumberOfElements(); i++) {
            switch (structure.getElementType(i)) {
                case PrimitiveType.BYTE:
                    if (line1.getByte(i) != line2.getByte(i)) {
                        return false;
                    }
                    break;
                case PrimitiveType.CHAR:
                    if (line1.getChar(i) != line2.getChar(i)) {
                        return false;
                    }
                    break;
                case PrimitiveType.SHORT:
                    if (line1.getShort(i) != line2.getShort(i)) {
                        return false;
                    }
                    break;
                case PrimitiveType.INT:
                    if (line1.getInt(i) != line2.getInt(i)) {
                        return false;
                    }
                    break;
                case PrimitiveType.LONG:
                    if (line1.getLong(i) != line2.getLong(i)) {
                        return false;
                    }
                    break;
                case PrimitiveType.FLOAT:
                    if (line1.getFloat(i) != line2.getFloat(i)) {
                        return false;
                    }
                    break;
                case PrimitiveType.DOUBLE:
                    if (line1.getDouble(i) != line2.getDouble(i)) {
                        return false;
                    }
                    break;
                default:
                    throw new IllegalStateException();
            }
        }
        return true;
    }

    public static <T> T[] mergeArrays(T[] firstHalf, T[] secondHalf) {
        T[] complete = (T[]) new Object[firstHalf.length + secondHalf.length];
        for (int i = 0; i < firstHalf.length; i++) {
            complete[i] = firstHalf[i];
        }
        for (int i = 0; i < secondHalf.length; i++) {
            complete[i + firstHalf.length] = secondHalf[i];
        }
        return complete;
    }

    public static byte[] mergeArrays(byte[] firstHalf, byte[] secondHalf) {
        byte[] complete = new byte[firstHalf.length + secondHalf.length];
        for (int i = 0; i < firstHalf.length; i++) {
            complete[i] = firstHalf[i];
        }
        for (int i = 0; i < secondHalf.length; i++) {
            complete[i + firstHalf.length] = secondHalf[i];
        }
        return complete;
    }

    /**
     * @param data
     * @return [firstHalf, secondHalf]
     */
    public static <T> T[][] splitArrayInHalfs(T[] data) {
        T[][] halfs = (T[][]) new Object[2][];
        T[] firstHalf = (T[]) new Object[data.length / 2];
        T[] secondHalf = (T[]) new Object[data.length - firstHalf.length];
        for (int i = 0; i < firstHalf.length; i++) {
            firstHalf[i] = data[i];
        }
        for (int i = 0; i < secondHalf.length; i++) {
            secondHalf[i] = data[firstHalf.length + i];
        }
        halfs[0] = firstHalf;
        halfs[1] = secondHalf;
        return halfs;
    }

    public static List<File> getAllFilesInDirectory(File directory, String extension) {
        List<File> fileList = new ArrayList<>();
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                if (extension == null) {
                    fileList.add(file);
                } else {
                    String name = file.getName();
                    int lastDotIndex = name.lastIndexOf(".");
                    if (lastDotIndex >= 0) {
                        String fileExtension = name.substring(lastDotIndex + 1);
                        if (extension.equalsIgnoreCase(fileExtension)) {
                            fileList.add(file);
                        }
                    }
                }
            }
        }
        return fileList;
    }
}
