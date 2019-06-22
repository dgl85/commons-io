package org.dgl.commons.io.tabular;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * Class to read several tabular files in one or more directories as one
 */
public class UnifiedTabularFileReader implements TabularReader {

    private final TabularFileReader[] sortedDataFileReaders;
    private final int numberOfLines;
    private final int[] fileBaseIndexes;

    /**
     * Files are sorted based on their full path
     *
     * @param directories
     * @param fileExtension include file with this extension only
     */
    public UnifiedTabularFileReader(File[] directories, String fileExtension) throws IOException {
        this(directories, fileExtension, Comparator.comparing(File::getAbsolutePath));
    }

    /**
     * Files are sorted based on their full path
     *
     * @param directory
     * @param fileExtension include file with this extension only
     */
    public UnifiedTabularFileReader(File directory, String fileExtension) throws IOException {
        this(new File[]{directory}, fileExtension, Comparator.comparing(File::getAbsolutePath));
    }

    /**
     * @param directories
     * @param fileExtension     include file with this extension only
     * @param sortingComparator comparator for file sorting
     * @throws IOException
     */
    public UnifiedTabularFileReader(File[] directories, String fileExtension, Comparator<File> sortingComparator)
            throws IOException {
        this(directories, (file) -> {
            String name = file.getName();
            int lastDotIndex = name.lastIndexOf(".");
            if (lastDotIndex >= 0) {
                String extension = name.substring(lastDotIndex + 1);
                return extension.equalsIgnoreCase(fileExtension);
            }
            return false;
        }, sortingComparator);
    }

    /**
     * @param directories
     * @param filterFunction    file is included if function returns true
     * @param sortingComparator comparator for file sorting
     * @throws IOException
     */
    public UnifiedTabularFileReader(File[] directories, Function<File, Boolean> filterFunction,
            Comparator<File> sortingComparator) throws IOException {
        SortedSet<File> sortedFiles = new TreeSet<>(sortingComparator);
        for (File path : directories) {
            if (!path.exists() || !path.isDirectory()) {
                throw new IllegalArgumentException();
            }
            List<File> selectedFiles = Utils.getAllFilesInDirectory(path, null);
            for (File selectedFile : selectedFiles) {
                boolean include = filterFunction.apply(selectedFile);
                if (include) {
                    sortedFiles.add(selectedFile);
                }
            }
        }
        sortedDataFileReaders = new TabularFileReader[sortedFiles.size()];
        fileBaseIndexes = new int[sortedFiles.size()];
        int lineCounter = 0;
        int indexCounter = 0;
        Iterator<File> filesIterator = sortedFiles.iterator();
        while (filesIterator.hasNext()) {
            sortedDataFileReaders[indexCounter] = new TabularFileReader(filesIterator.next().getAbsolutePath());
            lineCounter += sortedDataFileReaders[indexCounter].getNumberOfLines();
            fileBaseIndexes[indexCounter] = lineCounter;
            indexCounter++;
        }
        numberOfLines = lineCounter;
    }

    public DataLine getLine(int lineIndex) throws IOException {
        int[] indexes = getReaderAndLineIndex(lineIndex);
        return sortedDataFileReaders[indexes[0]].getLine(indexes[1]);
    }

    public byte[] getLineBytes(int lineIndex) throws IOException {
        int[] indexes = getReaderAndLineIndex(lineIndex);
        return sortedDataFileReaders[indexes[0]].getLineBytes(indexes[1]);
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public void close() throws IOException {
        for (TabularFileReader reader : sortedDataFileReaders) {
            reader.close();
        }
    }

    public void closeQuietly() {
        for (TabularFileReader reader : sortedDataFileReaders) {
            reader.closeQuietly();
        }
    }

    private int[] getReaderAndLineIndex(int virtualIndex) {
        int fileIndex = 0;
        while (fileBaseIndexes[fileIndex] <= virtualIndex) {
            fileIndex++;
        }
        long lineIndex = virtualIndex;
        if (fileIndex > 0) {
            lineIndex -= fileBaseIndexes[fileIndex - 1];
        }
        return new int[]{fileIndex, (int) lineIndex};
    }
}
