package EffectiveJava.chapter2.item9.keesun;

public class TestTryWithResource {

    public static void main(String[] args) {
        try (MyResource myResource = new MyResource();
             MyResource myResource1 = new MyResource();) {
            myResource.doSomething();
            myResource1.doSomething();
        }
        // 장점 1 : try with resource 와 AutoCloseable를 같이 사용하면 자동으로 close 호출 한다.
        // 장점 2 : 첫번째 에러 발생하고 두번째 에러 발생하면 먹히는게 아니고
        // 첫번째 에러 보여주고 두번째 에러를 차례로 보여주게 되어 디버깅이 굉장히 좋아진다.

    }
}
