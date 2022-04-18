package EffectiveJava.chapter3.item13.test;

import EffectiveJava.chapter3.item13.Stack;

public class B {
    final Object a = new Object();
    int b = 20;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        try {
            return  (B)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        B b = new B();

        B b2 = new B();

        System.out.println(b.a);
        System.out.println(b2.a);

        B b3 = (B) b.clone();
        System.out.println(b3.a);
    }
}
