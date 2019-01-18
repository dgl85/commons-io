package org.dgl.commons.io.tabular;

public class InvalidDataLineStructure extends RuntimeException {

    public InvalidDataLineStructure() {
        super();
    }

    public InvalidDataLineStructure(String message) {
        super(message);
    }
}
