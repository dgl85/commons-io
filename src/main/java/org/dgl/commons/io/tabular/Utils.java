package org.dgl.commons.io.tabular;

public class Utils {

    /**
     *
     * @param structure1
     * @param structure2
     * @return true if equal, false otherwise
     */
    public static boolean compareDataLineStructures(DataLineStructure structure1, DataLineStructure structure2) {
        boolean structuresAreEqual = true;
        if (structure1.getNumberOfElements() != structure2.getNumberOfElements()
                || structure1.getSizeInBytes() != structure2.getSizeInBytes()) {
            structuresAreEqual = false;
        } else {
            for (int i = 0; i < structure1.getNumberOfElements(); i++) {
                if (structure1.getElementType(i) != structure2.getElementType(i)) {
                    structuresAreEqual = false;
                    break;
                }
            }
        }
        return structuresAreEqual;
    }

    /**
     *
     * @param line1
     * @param line2
     * @return true if equal, false otherwise
     */
    public static boolean compareDataLines(DataLine line1, DataLine line2) {
        if (!compareDataLineStructures(line1.getLineStructure(), line2.getLineStructure())) {
            return false;
        }
        DataLineStructure structure = line1.getLineStructure();
        for (int i = 0; i < line1.getNumberOfElements(); i++) {
            switch (structure.getElementType(i)) {
                case PrimitiveType.BYTE:
                    if (line1.getByte(i) != line2.getByte(i)) {
                        return false;
                    }
                    break;
                case PrimitiveType.CHAR:
                    if (line1.getChar(i) != line2.getChar(i)) {
                        return false;
                    }
                    break;
                case PrimitiveType.SHORT:
                    if (line1.getShort(i) != line2.getShort(i)) {
                        return false;
                    }
                    break;
                case PrimitiveType.INT:
                    if (line1.getInt(i) != line2.getInt(i)) {
                        return false;
                    }
                    break;
                case PrimitiveType.LONG:
                    if (line1.getLong(i) != line2.getLong(i)) {
                        return false;
                    }
                    break;
                case PrimitiveType.FLOAT:
                    if (line1.getFloat(i) != line2.getFloat(i)) {
                        return false;
                    }
                    break;
                case PrimitiveType.DOUBLE:
                    if (line1.getDouble(i) != line2.getDouble(i)) {
                        return false;
                    }
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        return true;
    }

    public static DataLine[] mergeArrays(DataLine[] firstHalf, DataLine[] secondHalf) {
        DataLine[] complete = new DataLine[firstHalf.length+secondHalf.length];
        for (int i = 0; i < firstHalf.length; i++) {
            complete[i] = firstHalf[i];
        }
        for (int i = 0; i < secondHalf.length; i++) {
            complete[i+firstHalf.length] = secondHalf[i];
        }
        return complete;
    }

    /**
     *
     * @param data
     * @return [firstHalf, secondHalf]
     */
    public static DataLine[][] splitArrayInHalfs(DataLine[] data) {
        DataLine[][] halfs = new DataLine[2][];
        DataLine[] firstHalf = new DataLine[data.length/2];
        DataLine[] secondHalf = new DataLine[data.length-firstHalf.length];
        for (int i = 0; i < firstHalf.length; i++) {
            firstHalf[i] = data[i];
        }
        for (int i = 0; i < secondHalf.length; i++) {
            secondHalf[i] = data[firstHalf.length+i];
        }
        halfs[0] = firstHalf;
        halfs[1] = secondHalf;
        return halfs;
    }
}
