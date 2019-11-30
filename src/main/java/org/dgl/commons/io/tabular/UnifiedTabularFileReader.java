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
    private final long numberOfLines;
    private final long[] fileBaseIndexes;

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
     * @param directories
     * @param filterFunction file is included if function returns true
     */
    public UnifiedTabularFileReader(File[] directories, Function<File, Boolean> filterFunction) throws IOException {
        this(directories, filterFunction, Comparator.comparing(File::getAbsolutePath));
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
        SortedSet<File> sortedFiles = getSortedFiles(directories, filterFunction, sortingComparator);
        sortedDataFileReaders = new TabularFileReader[sortedFiles.size()];
        fileBaseIndexes = new long[sortedFiles.size()];
        long lineCounter = 0;
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

    public DataLine getLine(long lineIndex) throws IOException {
        long[] indexes = getReaderAndLineIndex(lineIndex);
        return sortedDataFileReaders[(int) indexes[0]].getLine(indexes[1]);
    }

    public byte[] getLineBytes(long lineIndex) throws IOException {
        long[] indexes = getReaderAndLineIndex(lineIndex);
        return sortedDataFileReaders[(int) indexes[0]].getLineBytes(indexes[1]);
    }

    public long getNumberOfLines() {
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

    private SortedSet<File> getSortedFiles(File[] directories, Function<File, Boolean> filterFunction,
            Comparator<File> sortingComparator) {
        SortedSet<File> sortedFiles = new TreeSet<>(sortingComparator);
        for (File path : directories) {
            if (!path.exists() || !path.isDirectory()) {
                throw new IllegalArgumentException();
            }
            List<File> allFiles = Utils.getAllFilesInDirectory(path, null);
            for (File file : allFiles) {
                boolean include = filterFunction.apply(file);
                if (include) {
                    sortedFiles.add(file);
                }
            }
        }
        return sortedFiles;
    }

    private long[] getReaderAndLineIndex(long virtualIndex) {
        int fileIndex = 0;
        while (fileBaseIndexes[fileIndex] <= virtualIndex) {
            fileIndex++;
        }
        long lineIndex = virtualIndex;
        if (fileIndex > 0) {
            lineIndex -= fileBaseIndexes[fileIndex - 1];
        }
        return new long[]{fileIndex, lineIndex};
    }
}
