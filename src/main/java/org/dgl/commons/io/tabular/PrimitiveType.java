package org.dgl.commons.io.tabular;

public class PrimitiveType {
    public static final byte BYTE = 0x01;
    public static final byte CHAR = 0x02;
    public static final byte SHORT = 0x03;
    public static final byte INT = 0x04;
    public static final byte LONG = 0x05;
    public static final byte FLOAT = 0x06;
    public static final byte DOUBLE = 0x07;

    public static int getLengthInBytesForType(byte type) {
        int length;
        switch (type) {
            case PrimitiveType.BYTE:
                length = 1;
                break;
            case PrimitiveType.CHAR:
                length = 2;
                break;
            case PrimitiveType.SHORT:
                length = 2;
                break;
            case PrimitiveType.INT:
                length = 4;
                break;
            case PrimitiveType.LONG:
                length = 8;
                break;
            case PrimitiveType.FLOAT:
                length = 4;
                break;
            case PrimitiveType.DOUBLE:
                length = 8;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return length;
    }
}
