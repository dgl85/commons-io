package org.dgl.commons.io.tabular;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;

public class FileReaderAndWriterTest {

    private static String tempFilePath = System.getProperty("java.io.tmpdir") + File.separator + "tabular_test.dat";

    @BeforeAll
    public static void deletePossibleTempFile() {
        File file = new File(tempFilePath);
        if (file.exists()) {
            if (!file.delete()) {
                throw new IllegalStateException();
            }
        }
    }

    @Test
    public void testReadAndWrite() throws IOException {
        DataLineStructure differentDataLineStructure = new DataLineStructure(1);
        DataLine differentDataLine = new DataLine(differentDataLineStructure);
        differentDataLine.setFloat(0, 20F);
        int numberOfLines = 100000;
        Person[] data = new Person[numberOfLines];

        //Getting random data
        for (int i = 0; i < data.length; i++) {
            data[i] = Person.getRandom();
        }

        //Writing random data to file
        FileWriter writer = new FileWriter(tempFilePath,data[0].getDataLineStructure());
        for (int i = 0; i < data.length; i++) {
            writer.writeLine(data[i].getDataLine());
        }
        assertTrue(writer.isOpen());
        writer.close();
        assertFalse(writer.isOpen());

        //Testing random data with getDataLine
        FileReader reader = new FileReader(tempFilePath);
        for (int i = 0; i < data.length; i++) {
            assertTrue(Utils.compareDataLines(data[i].getDataLine(), reader.getLine(i)));
        }

        //Testing random data with getDataLines
        DataLine[] lines = reader.getLines(0,reader.getNumberOfLines());
        assertEquals(data.length, lines.length);
        for (int i = 0; i < data.length; i++) {
            assertTrue(Utils.compareDataLines(data[i].getDataLine(), lines[i]));
        }
        assertTrue(reader.isOpen());
        reader.close();
        assertFalse(reader.isOpen());

        //Testing writeLine with index (replace) and writing to existing file
        writer = new FileWriter(tempFilePath,data[0].getDataLineStructure());
        for (int i = 0; i < data.length; i++) {
            writer.writeLine(i, data[i].getDataLine());
        }
        writer.close();
        reader = new FileReader(tempFilePath);
        for (int i = 0; i < data.length; i++) {
            assertTrue(Utils.compareDataLines(data[i].getDataLine(), reader.getLine(i)));
        }
        reader.close();

        //Testing writeLine without index (append) to an existing file
        writer = new FileWriter(tempFilePath,data[0].getDataLineStructure());
        for (int i = 0; i < data.length; i++) {
            writer.writeLine(data[i].getDataLine());
        }
        writer.close();
        reader = new FileReader(tempFilePath);
        for (int i = 0; i < data.length; i++) {
            assertTrue(Utils.compareDataLines(data[i].getDataLine(), reader.getLine(i)));
            assertTrue(Utils.compareDataLines(data[i].getDataLine(), reader.getLine(i+data.length)));
        }
        reader.close();

        //Testing writeLines
        writer = new FileWriter(tempFilePath,data[0].getDataLineStructure());
        writer.writeLines(lines);
        writer.close();
        reader = new FileReader(tempFilePath);
        for (int i = 0; i < data.length; i++) {
            assertTrue(Utils.compareDataLines(data[i].getDataLine(), reader.getLine(i)));
            assertTrue(Utils.compareDataLines(data[i].getDataLine(), reader.getLine(i+data.length)));
            assertTrue(Utils.compareDataLines(data[i].getDataLine(), reader.getLine(i+(data.length*2))));
        }
        reader.close();

        //Testing exceptions
        final FileWriter writer2 = new FileWriter(tempFilePath,data[0].getDataLineStructure());
        final FileReader reader2 = new FileReader(tempFilePath);
        assertThrows(IndexOutOfBoundsException.class, () -> writer2.writeLine(-1, lines[0]));
        assertThrows(IndexOutOfBoundsException.class, () -> writer2.writeLine(reader2.getNumberOfLines()+1, lines[0]));
        assertThrows(InvalidDataLineStructure.class, () -> writer2.writeLine(0, differentDataLine));
        writer.close();
        reader2.close();
        assertThrows(InvalidDataLineStructure.class, () -> new FileWriter(tempFilePath, differentDataLineStructure));
    }

    @Test
    public void testDataLine() {
        DataLine dataLine = Person.getRandom().getDataLine();
        DataLineStructure lineStructure = dataLine.getLineStructure();

        assertEquals(dataLine.getNumberOfElements(), lineStructure.getNumberOfElements());
        assertEquals(dataLine.getSizeInBytes(), lineStructure.getSizeInBytes());
        assertEquals(9, dataLine.getNumberOfElements());
        assertEquals((byte) 1, lineStructure.getElementType(0));
        assertEquals((byte) 2, lineStructure.getElementType(1));
        assertEquals((byte) 2, lineStructure.getElementType(2));
        assertEquals((byte) 2, lineStructure.getElementType(3));
        assertEquals((byte) 3, lineStructure.getElementType(4));
        assertEquals((byte) 4, lineStructure.getElementType(5));
        assertEquals((byte) 5, lineStructure.getElementType(6));
        assertEquals((byte) 6, lineStructure.getElementType(7));
        assertEquals((byte) 7, lineStructure.getElementType(8));

        assertThrows(IndexOutOfBoundsException.class, () -> dataLine.getDouble(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> dataLine.getDouble(dataLine.getNumberOfElements()));
        assertThrows(InvalidFormatException.class, () -> dataLine.getChar(0));
        assertThrows(InvalidFormatException.class, () -> dataLine.getShort(0));
        assertThrows(InvalidFormatException.class, () -> dataLine.getInt(0));
        assertThrows(InvalidFormatException.class, () -> dataLine.getLong(0));
        assertThrows(InvalidFormatException.class, () -> dataLine.getFloat(0));
        assertThrows(InvalidFormatException.class, () -> dataLine.getDouble(0));
        assertThrows(InvalidFormatException.class, () -> dataLine.getByte(1));

        assertThrows(IndexOutOfBoundsException.class, () -> dataLine.setDouble(-1, 1D));
        assertThrows(IndexOutOfBoundsException.class, () -> dataLine.setDouble(dataLine.getNumberOfElements(), 1D));
        assertThrows(InvalidFormatException.class, () -> dataLine.setChar(0, 'D'));
        assertThrows(InvalidFormatException.class, () -> dataLine.setShort(0, (short)1));
        assertThrows(InvalidFormatException.class, () -> dataLine.setInt(0, 1));
        assertThrows(InvalidFormatException.class, () -> dataLine.setLong(0, 1L));
        assertThrows(InvalidFormatException.class, () -> dataLine.setFloat(0, 1F));
        assertThrows(InvalidFormatException.class, () -> dataLine.setDouble(0, 1D));
        assertThrows(InvalidFormatException.class, () -> dataLine.setByte(1, (byte)1));

        assertThrows(IllegalArgumentException.class, () -> lineStructure.setAllType((byte)0));
        assertThrows(IllegalArgumentException.class, () -> lineStructure.setAllType((byte)8));
    }

    @AfterAll
    public static void deleteTempFile() {
        new File(tempFilePath).deleteOnExit();
    }
}
