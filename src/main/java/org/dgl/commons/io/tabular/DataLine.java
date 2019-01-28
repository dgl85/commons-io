package org.dgl.commons.io.tabular;

/**
 * Using primitive arrays for better performance
 */
public class DataLine {

    private final int numberOfElements;
    private final DataLineStructure lineStructure;
    private final byte[] elementsType;
    private final int[] elementsInternalIndex;
    private byte[] byteElements;
    private char[] charElements;
    private short[] shortElements;
    private int[] intElements;
    private long[] longElements;
    private float[] floatElements;
    private double[] doubleElements;

    public DataLine(DataLineStructure lineStructure) {
        this.lineStructure = lineStructure;
        numberOfElements = lineStructure.getNumberOfElements();
        elementsType = new byte[numberOfElements];
        elementsInternalIndex = new int[numberOfElements];
        initializeElementArrays();
    }

    public byte getByte(int elementIndex) {
        validateRequest(elementIndex, PrimitiveType.BYTE);
        return byteElements[getInternalIndex(elementIndex)];
    }

    public char getChar(int elementIndex) {
        validateRequest(elementIndex, PrimitiveType.CHAR);
        return charElements[getInternalIndex(elementIndex)];
    }

    public short getShort(int elementIndex) {
        validateRequest(elementIndex, PrimitiveType.SHORT);
        return shortElements[getInternalIndex(elementIndex)];
    }

    public int getInt(int elementIndex) {
        validateRequest(elementIndex, PrimitiveType.INT);
        return intElements[getInternalIndex(elementIndex)];
    }

    public long getLong(int elementIndex) {
        validateRequest(elementIndex, PrimitiveType.LONG);
        return longElements[getInternalIndex(elementIndex)];
    }

    public float getFloat(int elementIndex) {
        validateRequest(elementIndex, PrimitiveType.FLOAT);
        return floatElements[getInternalIndex(elementIndex)];
    }

    public double getDouble(int elementIndex) {
        validateRequest(elementIndex, PrimitiveType.DOUBLE);
        return doubleElements[getInternalIndex(elementIndex)];
    }

    public DataLine setByte(int elementIndex, byte data) {
        validateRequest(elementIndex, PrimitiveType.BYTE);
        byteElements[getInternalIndex(elementIndex)] = data;
        return this;
    }

    public DataLine setChar(int elementIndex, char data) {
        validateRequest(elementIndex, PrimitiveType.CHAR);
        charElements[getInternalIndex(elementIndex)] = data;
        return this;
    }

    public DataLine setShort(int elementIndex, short data) {
        validateRequest(elementIndex, PrimitiveType.SHORT);
        shortElements[getInternalIndex(elementIndex)] = data;
        return this;
    }

    public DataLine setInt(int elementIndex, int data) {
        validateRequest(elementIndex, PrimitiveType.INT);
        intElements[getInternalIndex(elementIndex)] = data;
        return this;
    }

    public DataLine setLong(int elementIndex, long data) {
        validateRequest(elementIndex, PrimitiveType.LONG);
        longElements[getInternalIndex(elementIndex)] = data;
        return this;
    }

    public DataLine setFloat(int elementIndex, float data) {
        validateRequest(elementIndex, PrimitiveType.FLOAT);
        floatElements[getInternalIndex(elementIndex)] = data;
        return this;
    }

    public DataLine setDouble(int elementIndex, double data) {
        validateRequest(elementIndex, PrimitiveType.DOUBLE);
        doubleElements[getInternalIndex(elementIndex)] = data;
        return this;
    }

    public int getSizeInBytes() {
        return lineStructure.getSizeInBytes();
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public DataLineStructure getLineStructure() {
        return lineStructure;
    }

    private void initializeElementArrays() {
        int byteCounter = 0;
        int charCounter = 0;
        int shortCounter = 0;
        int intCounter = 0;
        int longCounter = 0;
        int floatCounter = 0;
        int doubleCounter = 0;

        for (int i = 0; i < lineStructure.getNumberOfElements(); i++) {
            switch (lineStructure.getElementType(i)) {
                case PrimitiveType.BYTE:
                    elementsType[i] = PrimitiveType.BYTE;
                    elementsInternalIndex[i] = byteCounter;
                    byteCounter++;
                    break;
                case PrimitiveType.CHAR:
                    elementsType[i] = PrimitiveType.CHAR;
                    elementsInternalIndex[i] = charCounter;
                    charCounter++;
                    break;
                case PrimitiveType.SHORT:
                    elementsType[i] = PrimitiveType.SHORT;
                    elementsInternalIndex[i] = shortCounter;
                    shortCounter++;
                    break;
                case PrimitiveType.INT:
                    elementsType[i] = PrimitiveType.INT;
                    elementsInternalIndex[i] = intCounter;
                    intCounter++;
                    break;
                case PrimitiveType.LONG:
                    elementsType[i] = PrimitiveType.LONG;
                    elementsInternalIndex[i] = longCounter;
                    longCounter++;
                    break;
                case PrimitiveType.FLOAT:
                    elementsType[i] = PrimitiveType.FLOAT;
                    elementsInternalIndex[i] = floatCounter;
                    floatCounter++;
                    break;
                case PrimitiveType.DOUBLE:
                    elementsType[i] = PrimitiveType.DOUBLE;
                    elementsInternalIndex[i] = doubleCounter;
                    doubleCounter++;
                    break;
            }
        }
        byteElements = new byte[byteCounter];
        charElements = new char[charCounter];
        shortElements = new short[shortCounter];
        intElements = new int[intCounter];
        longElements = new long[longCounter];
        floatElements = new float[floatCounter];
        doubleElements = new double[doubleCounter];
    }

    private int getInternalIndex(int elementIndex) {
        return elementsInternalIndex[elementIndex];
    }

    private void validateRequest(int elementIndex, byte type) {
        if (elementIndex < 0 || elementIndex >= numberOfElements) {
            throw new IndexOutOfBoundsException();
        }
        if (type != elementsType[elementIndex]) {
            throw new InvalidFormatException();
        }
    }

}
