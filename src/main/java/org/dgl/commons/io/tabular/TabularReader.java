package org.dgl.commons.io.tabular;

public interface TabularReader {

    DataLine getLine(int lineIndex) throws Exception;

    int getNumberOfLines();

    void close() throws Exception;

    void closeQuietly();
}
