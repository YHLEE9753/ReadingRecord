package EffectiveJava.chapter2.item8;

public class SampleRunner {

    public static void main(String[] args) throws InterruptedException {
        SampleRunner runner = new SampleRunner();
        runner.run();
        Thread.sleep(1000L);
        // run 하고 1초후에 끝나는데 이게
        // 이게 GC 의 대상이지만 GC 가 된다는 보장은 없다.
        // GC 가 바로바로 된다는건 모른다.
        // 즉 언제 finalize 가 호출되는지를 알 수가 없다.
        // 또한 성능에 문제가 발생한다.
        // 어떤 객체를 GC 할때 finalize 안에 있는 코드를 실행해야 하는데
        // 실행하는데 시간이 걸려 GC 에 문제가 생긴다(성능적)

        System.gc();
        // System.gc() 호출해도 된다는 보장은 없다. 하지만 되네? ㅋㅋ
        // 실제로 안되는 경우도 있다.
        }

    private void run() {
        FinalizerExample finalizerExample = new FinalizerExample();
        finalizerExample.hello();
    }
}
