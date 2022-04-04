package EffectiveJava.chapter2.item9.keesun;

public class MyResource implements AutoCloseable{

    public void doSomething(){
        System.out.println("doing something");
        throw new FirstException();
    }

    @Override
    public void close(){
        System.out.println("clean my resource");
        throw new SecondException();
    }
}
