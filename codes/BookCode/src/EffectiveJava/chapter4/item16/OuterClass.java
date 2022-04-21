package EffectiveJava.chapter4.item16;

public class OuterClass {

    private int open = 10;
    private int close = 20;

    private class InnerClass{
        public int open2 = 100;
        public int close2 = 20;

        public InnerClass(int open2, int close2) {
            this.open2 = open2;
            this.close2 = close2;
        }
    }
}
