package EffectiveJava.chapter3.item13;

public class A implements Cloneable {
    int num;

    public A(int num) {
        this.num = num;
    }

    public A() {
    }

    @Override
    public A clone() {
        try {
            //num 만 깊은 복사
            Object clone = super.clone();
            return (A)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) {

    }
}