package org.dgl.commons.io.tabular;

import java.io.Closeable;
import java.io.IOException;

public interface TabularReader extends Closeable {

    DataLine getLine(long lineIndex) throws Exception;

    long getNumberOfLines();

    void close() throws IOException;

    void closeQuietly();
}
