package org.dgl.commons.io.tabular;

public class InvalidFormatException extends RuntimeException {

    public InvalidFormatException() {
        super();
    }

    public InvalidFormatException(String message) {
        super(message);
    }
}