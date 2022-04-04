package EffectiveJava.chapter2.item8;

public class SampleRunner3 {

    public static void main(String[] args) {
        SampleRunner3 sampleRunner3 = new SampleRunner3();
        sampleRunner3.run();
        System.gc();
    }

    private void run() {
        CleanerSample cleanerSample = new CleanerSample();
        cleanerSample.doSomething();
    }
}
