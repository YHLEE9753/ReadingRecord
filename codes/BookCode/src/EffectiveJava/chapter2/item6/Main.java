package EffectiveJava.chapter2.item6;

public class Main {
    public static void main(String[] args) {
        Boolean true1 = Boolean.valueOf("true");
        Boolean true2 = Boolean.valueOf("true");

        System.out.println(true1 == true2); //true 반환
        System.out.println(Boolean.TRUE);
    }
}
