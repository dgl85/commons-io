package org.dgl.commons.io.tabular;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Helper class to facilitate testing and exemplify a toy wrapper around DataLineStructure
 */
public class Person {
    private final byte numberOfChildren;
    private final char nameInitial;
    private final char middleInitial;
    private final char surnameInitial;
    private final short age;
    private final int numberOfFriends;
    private final long traveledMiles;
    private final float creditScore;
    private final double moneyInBank;
    private static final DataLineStructure dataLineStructure = new DataLineStructure(new byte[]{
            PrimitiveType.BYTE,PrimitiveType.CHAR,PrimitiveType.CHAR,PrimitiveType.CHAR,PrimitiveType.SHORT,
            PrimitiveType.INT,PrimitiveType.LONG,PrimitiveType.FLOAT,PrimitiveType.DOUBLE});
    private final DataLine dataLine;

    public Person(byte numberOfChildren, char nameInitial, char middleInitial, char surnameInitial, short age,
                  int numberOfFriends, long traveledMiles, float creditScore, double moneyInBank) {
        this.numberOfChildren = numberOfChildren;
        this.nameInitial = nameInitial;
        this.middleInitial = middleInitial;
        this.surnameInitial = surnameInitial;
        this.age = age;
        this.numberOfFriends = numberOfFriends;
        this.traveledMiles = traveledMiles;
        this.creditScore = creditScore;
        this.moneyInBank = moneyInBank;

        dataLine = new DataLine(dataLineStructure);
        dataLine.setByte(0, numberOfChildren);
        dataLine.setChar(1, nameInitial);
        dataLine.setChar(2, middleInitial);
        dataLine.setChar(3, surnameInitial);
        dataLine.setShort(4, age);
        dataLine.setInt(5, numberOfFriends);
        dataLine.setLong(6, traveledMiles);
        dataLine.setFloat(7, creditScore);
        dataLine.setDouble(8, moneyInBank);
    }

    public byte getNumberOfChildren() {
        return numberOfChildren;
    }

    public char getNameInitial() {
        return nameInitial;
    }

    public char getMiddleInitial() {
        return middleInitial;
    }

    public char getSurnameInitial() {
        return surnameInitial;
    }

    public short getAge() {
        return age;
    }

    public int getNumberOfFriends() {
        return numberOfFriends;
    }

    public long getTraveledMiles() {
        return traveledMiles;
    }

    public float getCreditScore() {
        return creditScore;
    }

    public double getMoneyInBank() {
        return moneyInBank;
    }

    public DataLineStructure getDataLineStructure() {
        return dataLineStructure;
    }

    public DataLine getDataLine() {
        return dataLine;
    }

    public static Person getRandom() {
        byte numberOfChildren = (byte) (ThreadLocalRandom.current().nextInt(255)-128);
        char nameInitial = (char) (ThreadLocalRandom.current().nextInt(65536));
        char middleInitial = (char) (ThreadLocalRandom.current().nextInt(65536));
        char surnameInitial = (char) (ThreadLocalRandom.current().nextInt(65536));
        short age = (short) (ThreadLocalRandom.current().nextInt(65535)-32768);
        int numberOfFriends = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE,Integer.MAX_VALUE);
        long traveledMiles = ThreadLocalRandom.current().nextLong(Long.MIN_VALUE,Long.MAX_VALUE);
        float creditScore = ThreadLocalRandom.current().nextFloat()
                * (numberOfChildren > 128 ? Float.MAX_VALUE : Float.MIN_VALUE);
        double moneyInBank = ThreadLocalRandom.current().nextDouble(Double.MIN_VALUE,Double.MAX_VALUE);

        return new Person(numberOfChildren, nameInitial, middleInitial, surnameInitial, age, numberOfFriends,
                traveledMiles, creditScore, moneyInBank);
    }
}
