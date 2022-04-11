package EffectiveJava.chapter3.item12;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {

        BigInteger bigNum1 = new BigInteger("123456789");

        BigInteger bigNum2 = new BigInteger("10101110", 2);

        long mylong = 987654321;
        BigInteger bigNum3 = BigInteger.valueOf(mylong);

        System.out.println("bigNum1 : " + bigNum1);
        System.out.println("bigNum2 : " + bigNum2.toString());
        System.out.println("bigNum3 : " + bigNum3.toString());

    }
}
