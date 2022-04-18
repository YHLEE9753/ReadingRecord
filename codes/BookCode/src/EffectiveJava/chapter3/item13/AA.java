package EffectiveJava.chapter3.item13;


public class AA implements Cloneable {
    int num;
    final Object oo;

    public AA(int num, Object oo) {
        this.num = num;
        this.oo = oo;
    }

    @Override
    public AA clone() {
        try {
            //num 만 깊은 복사
            Object clone = super.clone();
            return (AA)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) {
        AA a = new AA(12, new Object());
        AA a2 = new AA(13, new Object());
        AA cloneA = a.clone();


        System.out.println("a      : " + a.oo);
        System.out.println("a2     : " + a2.oo);
        System.out.println("cloneA : " + cloneA.oo);
    }
}