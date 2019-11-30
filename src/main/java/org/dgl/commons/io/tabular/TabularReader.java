package org.dgl.commons.io.tabular;

public interface TabularReader {

    DataLine getLine(long lineIndex) throws Exception;

    long getNumberOfLines();

    void close() throws Exception;

    void closeQuietly();
}
