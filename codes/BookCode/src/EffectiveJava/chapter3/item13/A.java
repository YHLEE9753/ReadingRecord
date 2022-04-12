package EffectiveJava.chapter3.item13;

public class A implements Cloneable {
    int num;

    public A() {
        System.out.println("---------------------");
        System.out.println("A constructor");
        System.out.println("---------------------");
    }

    public A(int num) {
        System.out.println("---------------------");
        System.out.println("A constructor");
        System.out.println("---------------------");
        this.num = num;
    }

    @Override
    public A clone() {
        try {
            System.out.println("---------------------");
            System.out.println("A Clone");
            System.out.println("---------------------");
            Object clone = super.clone();
            return (A)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public String toString() {
        return "A{" +
                "num=" + num +
                '}';
    }

    public static void main(String[] args) {
        A a = new A(12);
        A cloneA = a.clone();

        System.out.println(a);
        System.out.println(cloneA);
        System.out.println(a==cloneA);
        System.out.println(a.equals(cloneA));
    }
}