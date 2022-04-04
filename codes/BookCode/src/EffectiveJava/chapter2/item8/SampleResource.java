package EffectiveJava.chapter2.item8;

public class SampleResource implements AutoCloseable{

    private boolean closed;

    @Override
    public void close() throws Exception {
        if(this.closed){
            throw new IllegalStateException();
        }
        closed = true;
        System.out.println("close");
    }

    public void hello(){
        System.out.println("hi");
    }

    // closing 을 클라이언트가 안했을수도 있으므로
    // 안전망 삼아 finalize 에서 close 를 한다.
    @Override
    protected void finalize() throws Throwable {
        if(!this.closed){
            close();
        }
    }
}
