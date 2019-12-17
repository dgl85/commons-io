package org.dgl.commons.io.tabular;

import javafx.util.Pair;
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

    public static DataLine[] mergeArrays(DataLine[] firstHalf, DataLine[] secondHalf) {
        DataLine[] complete = new DataLine[firstHalf.length + secondHalf.length];
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

    public static Pair<DataLine[], DataLine[]> splitArrayInHalf(DataLine[] data) {
        DataLine[] firstHalf = new DataLine[data.length / 2];
        DataLine[] secondHalf = new DataLine[data.length - firstHalf.length];
        for (int i = 0; i < firstHalf.length; i++) {
            firstHalf[i] = data[i];
        }
        for (int i = 0; i < secondHalf.length; i++) {
            secondHalf[i] = data[firstHalf.length + i];
        }
        return new Pair<>(firstHalf, secondHalf);
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
