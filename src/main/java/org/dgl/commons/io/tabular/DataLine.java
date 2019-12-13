package org.dgl.commons.io.tabular;

/**
 * Using primitive arrays for better performance
 */
public class DataLine {

    protected final int numberOfElements;
    protected final DataLineStructure lineStructure;
    protected final byte[] elementsType;
    protected final int[] elementsInternalIndex;
    protected byte[] byteElements;
    protected char[] charElements;
    protected short[] shortElements;
    protected int[] intElements;
    protected long[] longElements;
    protected float[] floatElements;
    protected double[] doubleElements;

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

    protected void initializeElementArrays() {
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
                default:
                    throw new IllegalArgumentException();
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

    protected int getInternalIndex(int elementIndex) {
        return elementsInternalIndex[elementIndex];
    }

    protected void validateRequest(int elementIndex, byte type) {
        if (elementIndex < 0 || elementIndex >= numberOfElements) {
            throw new IndexOutOfBoundsException();
        }
        if (type != elementsType[elementIndex]) {
            throw new InvalidFormatException();
        }
    }

    public boolean equals(DataLine line) {
        if (!getLineStructure().equals(line.getLineStructure())) {
            return false;
        }
        byte[] bytes1 = Utils.getDataLineBytes(this);
        byte[] bytes2 = Utils.getDataLineBytes(line);
        for (int i = 0; i < bytes1.length; i++) {
            if (bytes1[i] != bytes2[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String returnString = "";
        for (int i = 0; i < lineStructure.getNumberOfElements(); i++) {
            String element = (i > 0 ? "," : "");
            switch (lineStructure.getElementType(i)) {
                case PrimitiveType.BYTE:
                    element = element.concat(Byte.toString(getByte(i)));
                    break;
                case PrimitiveType.CHAR:
                    element = element.concat(Character.toString(getChar(i)));
                    break;
                case PrimitiveType.SHORT:
                    element = element.concat(Short.toString(getShort(i)));
                    break;
                case PrimitiveType.INT:
                    element = element.concat(Integer.toString(getInt(i)));
                    break;
                case PrimitiveType.LONG:
                    element = element.concat(Long.toString(getLong(i)));
                    break;
                case PrimitiveType.FLOAT:
                    element = element.concat(Float.toString(getFloat(i)));
                    break;
                case PrimitiveType.DOUBLE:
                    element = element.concat(Double.toString(getDouble(i)));
                    break;
                default:
                    throw new IllegalStateException();
            }
            returnString = returnString.concat(element);
        }
        return returnString;
    }

}
