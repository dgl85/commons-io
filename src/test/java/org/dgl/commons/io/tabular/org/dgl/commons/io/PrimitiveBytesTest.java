package org.dgl.commons.io.tabular.org.dgl.commons.io;

import org.dgl.commons.io.PrimitiveBytes;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class PrimitiveBytesTest {

    @Test
    public void simpleTest() {
        ByteOrder order = ByteOrder.BIG_ENDIAN;
        for (int i = 0; i < 2; i++) {
            ByteBuffer buffer = ByteBuffer.allocate(8).order(order);
            //char
            char ch = 'D';
            byte[] bytes = new byte[2];
            PrimitiveBytes.putCharBytesInArray(ch, bytes, order);
            buffer.clear();
            buffer.put(bytes);
            buffer.flip();
            assertEquals(ch, buffer.getChar());
            assertEquals(ch, PrimitiveBytes.getChar(bytes, order));
            //short
            short sh = (short) 20;
            bytes = new byte[2];
            PrimitiveBytes.putShortBytesInArray(sh, bytes, order);
            buffer.clear();
            buffer.put(bytes);
            buffer.flip();
            assertEquals(sh, buffer.getShort());
            assertEquals(sh, PrimitiveBytes.getShort(bytes, order));
            //int
            int number = 20;
            bytes = new byte[4];
            PrimitiveBytes.putIntBytesInArray(number, bytes, order);
            buffer.clear();
            buffer.put(bytes);
            buffer.flip();
            assertEquals(number, buffer.getInt());
            assertEquals(number, PrimitiveBytes.getInt(bytes, order));
            //long
            long lng = 1234567890987654321L;
            bytes = new byte[8];
            PrimitiveBytes.putLongBytesInArray(lng, bytes, order);
            buffer.clear();
            buffer.put(bytes);
            buffer.flip();
            assertEquals(lng, buffer.getLong());
            assertEquals(lng, PrimitiveBytes.getLong(bytes, order));
            //float
            float flt = 1.85F;
            bytes = new byte[4];
            PrimitiveBytes.putFloatBytesInArray(flt, bytes, order);
            buffer.clear();
            buffer.put(bytes);
            buffer.flip();
            assertEquals(flt, buffer.getFloat());
            assertEquals(flt, PrimitiveBytes.getFloat(bytes, order));
            //double
            double dbl = 123456789.0987654321D;
            bytes = new byte[8];
            PrimitiveBytes.putDoubleBytesInArray(dbl, bytes, order);
            buffer.clear();
            buffer.put(bytes);
            buffer.flip();
            assertEquals(dbl, buffer.getDouble());
            assertEquals(dbl, PrimitiveBytes.getDouble(bytes, order));

            //Changing order
            order = ByteOrder.LITTLE_ENDIAN;
        }
    }
}
