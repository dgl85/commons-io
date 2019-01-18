package org.dgl.commons.io.tabular;

public class DataLineStructure {

    private static final byte DEFAULT_DATA_TYPE = PrimitiveType.FLOAT;
    private int sizeInBytes = 0;
    private final byte[] elementTypes;

    public DataLineStructure(int length) {
        elementTypes = new byte[length];
        for (int i = 0; i < length; i++){
            setElementType(i, DEFAULT_DATA_TYPE);
        }
    }

    public DataLineStructure(byte ...  primitiveTypes) {
        elementTypes = new byte[primitiveTypes.length];
        for (int i = 0; i < elementTypes.length; i++) {
            setElementType(i, primitiveTypes[i]);
        }
    }

    public void setElementType(int index, byte type) {
        if (type < 1 || type > 7) {
            throw new IllegalArgumentException();
        }
        int oldTypeSize = 0;
        if (elementTypes[index] != 0) {
            oldTypeSize = PrimitiveType.getLengthInBytesForType(elementTypes[index]);
        }
        sizeInBytes += PrimitiveType.getLengthInBytesForType(type)-oldTypeSize;
        elementTypes[index] = type;
    }

    /**
     * @param startIndex inclusive
     * @param endIndex exclusive
     * @param type
     */
    public void setRangeType(int startIndex, int endIndex, byte type) {
        for (int i = startIndex; i < endIndex; i++) {
            setElementType(i,type);
        }
    }

    public void setAllType(byte type) {
        setRangeType(0,getNumberOfElements(),type);
    }

    public int getNumberOfElements() {
        return elementTypes.length;
    }

    public byte getElementType(int index) {
        return elementTypes[index];
    }

    public int getSizeInBytes()
    {
        return sizeInBytes;
    }
}
