package org.dgl.commons.io;

import java.nio.ByteOrder;

/**
 * Direct Conversions between byte[] <-> primitives
 */
public class PrimitiveBytes {

    public static final ByteOrder DEFAULT_ENDIANNESS = ByteOrder.BIG_ENDIAN;


    //-------------------------------------------CHAR-------------------------------------------\\
    public static char getChar(byte[] bytes, int offset, ByteOrder endianness) {
        if (endianness == ByteOrder.BIG_ENDIAN) {
            return makeChar(bytes[offset],bytes[offset+1]);
        } else {
            return makeChar(bytes[offset+1],bytes[offset]);
        }
    }

    public static char getChar(byte[] bytes, int offset) {
        return getChar(bytes,offset, DEFAULT_ENDIANNESS);
    }

    public static char getChar(byte[] bytes, ByteOrder endianness) {
        return getChar(bytes,0,endianness);
    }

    public static char getChar(byte[] bytes) {
        return getChar(bytes,0, DEFAULT_ENDIANNESS);
    }

    private static char makeChar(byte b0, byte b1) {
        return (char) ((b0    << 8) |
                       (b1 & 0xff));
    }

    public static void putCharBytesInArray(char primitive, byte[] byteArray, int offset, ByteOrder endianness) {
        int currentOffset;
        int indexIncrements;
        int primitiveSizeInBytes = 2;

        if (endianness == ByteOrder.BIG_ENDIAN) {
            currentOffset = offset;
            indexIncrements = 1;
        } else {
            currentOffset = offset+primitiveSizeInBytes-1;
            indexIncrements = -1;
        }
        for (int i = (primitiveSizeInBytes-1)*8; i >= 0; i -=8 ) {
            byteArray[currentOffset] = (byte)(primitive >> i);
            currentOffset += indexIncrements;
        }
    }

    public static void putCharBytesInArray(char primitive, byte[] bytes, int offset) {
        putCharBytesInArray(primitive, bytes, offset, DEFAULT_ENDIANNESS);
    }

    public static void putCharBytesInArray(char primitive, byte[] bytes) {
        putCharBytesInArray(primitive, bytes, 0, DEFAULT_ENDIANNESS);
    }

    public static void putCharBytesInArray(char primitive, byte[] bytes, ByteOrder endianness) {
        putCharBytesInArray(primitive, bytes, 0, endianness);
    }

    //-------------------------------------------SHORT-------------------------------------------\\
    public static short getShort(byte[] bytes, int offset, ByteOrder endianness) {
        if (endianness == ByteOrder.BIG_ENDIAN) {
            return makeShort(bytes[offset],bytes[offset+1]);
        } else {
            return makeShort(bytes[offset+1],bytes[offset]);
        }
    }

    public static short getShort(byte[] bytes, int offset) {
        return getShort(bytes,offset, DEFAULT_ENDIANNESS);
    }

    public static short getShort(byte[] bytes, ByteOrder endianness) {
        return getShort(bytes,0, endianness);
    }

    public static short getShort(byte[] bytes) {
        return getShort(bytes,0, DEFAULT_ENDIANNESS);
    }

    private static short makeShort(byte b0, byte b1) {
        return  (short) ((b0    << 8) |
                         (b1 & 0xff));
    }

    public static void putShortBytesInArray(short primitive, byte[] byteArray, int offset, ByteOrder endianness) {
        int currentOffset;
        int indexIncrements;
        int primitiveSizeInBytes = 2;

        if (endianness == ByteOrder.BIG_ENDIAN) {
            currentOffset = offset;
            indexIncrements = 1;
        } else {
            currentOffset = offset+primitiveSizeInBytes-1;
            indexIncrements = -1;
        }
        for (int i = (primitiveSizeInBytes-1)*8; i >= 0; i -=8 ) {
            byteArray[currentOffset] = (byte)(primitive >> i);
            currentOffset += indexIncrements;
        }
    }

    public static void putShortBytesInArray(short primitive, byte[] bytes, int offset) {
        putShortBytesInArray(primitive, bytes, offset, DEFAULT_ENDIANNESS);
    }

    public static void putShortBytesInArray(short primitive, byte[] bytes) {
        putShortBytesInArray(primitive, bytes, 0, DEFAULT_ENDIANNESS);
    }

    public static void putShortBytesInArray(short primitive, byte[] bytes, ByteOrder endianness) {
        putShortBytesInArray(primitive, bytes, 0, endianness);
    }

    //-------------------------------------------INT-------------------------------------------\\
    public static int getInt(byte[] bytes, int offset, ByteOrder endianness) {
        if (endianness == ByteOrder.BIG_ENDIAN) {
            return makeInt(bytes[offset],bytes[offset+1],bytes[offset+2],bytes[offset+3]);
        } else {
            return makeInt(bytes[offset+3],bytes[offset+2],bytes[offset+1],bytes[offset]);
        }
    }

    public static int getInt(byte[] bytes, int offset) {
        return getInt(bytes,offset, DEFAULT_ENDIANNESS);
    }

    public static int getInt(byte[] bytes, ByteOrder endianness) {
        return getInt(bytes,0, endianness);
    }

    public static int getInt(byte[] bytes) {
        return getInt(bytes,0, DEFAULT_ENDIANNESS);
    }

    private static int makeInt(byte b0, byte b1, byte b2, byte b3) {
        return  ((b0          << 24) |
                ((b1 & 0xff)  << 16) |
                ((b2 & 0xff)  <<  8) |
                 (b3 & 0xff));
    }

    public static void putIntBytesInArray(int primitive, byte[] byteArray, int offset, ByteOrder endianness) {
        int currentOffset;
        int indexIncrements;
        int primitiveSizeInBytes = 4;

        if (endianness == ByteOrder.BIG_ENDIAN) {
            currentOffset = offset;
            indexIncrements = 1;
        } else {
            currentOffset = offset+primitiveSizeInBytes-1;
            indexIncrements = -1;
        }
        for (int i = (primitiveSizeInBytes-1)*8; i >= 0; i -=8 ) {
            byteArray[currentOffset] = (byte)(primitive >> i);
            currentOffset += indexIncrements;
        }
    }

    public static void putIntBytesInArray(int primitive, byte[] bytes, int offset) {
        putIntBytesInArray(primitive, bytes, offset, DEFAULT_ENDIANNESS);
    }

    public static void putIntBytesInArray(int primitive, byte[] bytes) {
        putIntBytesInArray(primitive, bytes, 0, DEFAULT_ENDIANNESS);
    }

    public static void putIntBytesInArray(int primitive, byte[] bytes, ByteOrder endianness) {
        putIntBytesInArray(primitive, bytes, 0, endianness);
    }

    //-------------------------------------------LONG-------------------------------------------\\
    public static long getLong(byte[] bytes, int offset, ByteOrder endianness) {
        if (endianness == ByteOrder.BIG_ENDIAN) {
            return makeLong(bytes[offset],bytes[offset+1],bytes[offset+2],bytes[offset+3],
                            bytes[offset+4],bytes[offset+5],bytes[offset+6],bytes[offset+7]);
        } else {
            return makeLong(bytes[offset+7],bytes[offset+6],bytes[offset+5],bytes[offset+4],
                            bytes[offset+3],bytes[offset+2],bytes[offset+1],bytes[offset]);
        }
    }

    public static long getLong(byte[] bytes, int offset) {
        return getLong(bytes,offset, DEFAULT_ENDIANNESS);
    }

    public static long getLong(byte[] bytes, ByteOrder endianness) {
        return getLong(bytes,0, endianness);
    }

    public static long getLong(byte[] bytes) {
        return getLong(bytes,0, DEFAULT_ENDIANNESS);
    }

    private static long makeLong(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7) {
        return  (((long)b0          << 56) |
                (((long)b1 & 0xff)  << 48) |
                (((long)b2 & 0xff)  << 40) |
                (((long)b3 & 0xff)  << 32) |
                (((long)b4 & 0xff)  << 24) |
                (((long)b5 & 0xff)  << 16) |
                (((long)b6 & 0xff)  <<  8) |
                 ((long)b7 & 0xff));
    }

    public static void putLongBytesInArray(long primitive, byte[] byteArray, int offset, ByteOrder endianness) {
        int currentOffset;
        int indexIncrements;
        int primitiveSizeInBytes = 8;

        if (endianness == ByteOrder.BIG_ENDIAN) {
            currentOffset = offset;
            indexIncrements = 1;
        } else {
            currentOffset = offset+primitiveSizeInBytes-1;
            indexIncrements = -1;
        }
        for (int i = (primitiveSizeInBytes-1)*8; i >= 0; i -=8 ) {
            byteArray[currentOffset] = (byte)(primitive >> i);
            currentOffset += indexIncrements;
        }
    }

    public static void putLongBytesInArray(long primitive, byte[] bytes, int offset) {
        putLongBytesInArray(primitive, bytes, offset, DEFAULT_ENDIANNESS);
    }

    public static void putLongBytesInArray(long primitive, byte[] bytes) {
        putLongBytesInArray(primitive, bytes, 0, DEFAULT_ENDIANNESS);
    }

    public static void putLongBytesInArray(long primitive, byte[] bytes, ByteOrder endianness) {
        putLongBytesInArray(primitive, bytes, 0, endianness);
    }

    //-------------------------------------------FLOAT-------------------------------------------\\
    public static float getFloat(byte[] bytes, int offset, ByteOrder endianness) {
        if (endianness == ByteOrder.BIG_ENDIAN) {
            return Float.intBitsToFloat(makeInt(bytes[offset],bytes[offset+1],
                    bytes[offset+2],bytes[offset+3]));
        } else {
            return Float.intBitsToFloat(makeInt(bytes[offset+3],bytes[offset+2],
                    bytes[offset+1],bytes[offset]));
        }
    }

    public static float getFloat(byte[] bytes, int offset) {
        return getFloat(bytes,offset, DEFAULT_ENDIANNESS);
    }

    public static float getFloat(byte[] bytes, ByteOrder endianness) {
        return getFloat(bytes,0, endianness);
    }

    public static float getFloat(byte[] bytes) {
        return getFloat(bytes,0, DEFAULT_ENDIANNESS);
    }

    public static void putFloatBytesInArray(float primitive, byte[] byteArray, int offset, ByteOrder endianness) {
        putIntBytesInArray(Float.floatToRawIntBits(primitive),byteArray,offset,endianness);
    }

    public static void putFloatBytesInArray(float primitive, byte[] bytes, int offset) {
        putFloatBytesInArray(primitive, bytes, offset, DEFAULT_ENDIANNESS);
    }

    public static void putFloatBytesInArray(float primitive, byte[] bytes) {
        putFloatBytesInArray(primitive, bytes, 0, DEFAULT_ENDIANNESS);
    }

    public static void putFloatBytesInArray(float primitive, byte[] bytes, ByteOrder endianness) {
        putFloatBytesInArray(primitive, bytes, 0, endianness);
    }

    //-------------------------------------------DOUBLE-------------------------------------------\\
    public static double getDouble(byte[] bytes, int offset, ByteOrder endianness) {
        if (endianness == ByteOrder.BIG_ENDIAN) {
            return Double.longBitsToDouble(makeLong(bytes[offset],bytes[offset+1],
                    bytes[offset+2],bytes[offset+3],bytes[offset+4],
                    bytes[offset+5],bytes[offset+6],bytes[offset+7]));
        } else {
            return Double.longBitsToDouble(makeLong(bytes[offset+7],bytes[offset+6],
                    bytes[offset+5],bytes[offset+4], bytes[offset+3],
                    bytes[offset+2],bytes[offset+1],bytes[offset]));
        }
    }

    public static double getDouble(byte[] bytes, int offset) {
        return getDouble(bytes,offset, DEFAULT_ENDIANNESS);
    }

    public static double getDouble(byte[] bytes, ByteOrder endianness) {
        return getDouble(bytes,0, endianness);
    }

    public static double getDouble(byte[] bytes) {
        return getDouble(bytes,0, DEFAULT_ENDIANNESS);
    }

    public static void putDoubleBytesInArray(double primitive, byte[] byteArray, int offset, ByteOrder endianness) {
        putLongBytesInArray(Double.doubleToRawLongBits(primitive),byteArray,offset,endianness);
    }

    public static void putDoubleBytesInArray(double primitive, byte[] bytes, int offset) {
        putDoubleBytesInArray(primitive, bytes, offset, DEFAULT_ENDIANNESS);
    }

    public static void putDoubleBytesInArray(double primitive, byte[] bytes) {
        putDoubleBytesInArray(primitive, bytes, 0, DEFAULT_ENDIANNESS);
    }

    public static void putDoubleBytesInArray(double primitive, byte[] bytes, ByteOrder endianness) {
        putDoubleBytesInArray(primitive, bytes, 0, endianness);
    }
}
