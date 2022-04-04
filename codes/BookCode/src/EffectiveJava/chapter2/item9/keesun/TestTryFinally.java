package EffectiveJava.chapter2.item9.keesun;

public class TestTryFinally {

    public static void main(String[] args) {
        // case1. 에러가 발생하여 close 하지 않는다.
//        MyResource myResource = new MyResource();
//        myResource.doSomething();
//        myResource.close();

        // case2.1. try finally
//        MyResource myResource = new MyResource();
//        try {
//            // 문제1 : 코드가 장황하다.
//            myResource.doSomething();
//            MyResource myResource1 = null;
//            // 문제2 : nested 하게 try 를 쓰게 된다.
//            try {
//                myResource1 = new MyResource();
//                myResource1.doSomething();
//            } finally {
//                if (myResource1 != null) {
//                    myResource1.close();
//                }
//            }
//        } finally {
//            // 에러가 발생하든 말든 일단 close 시킨다.
//            myResource.close();
//        }

        // case2.2 try finally
        MyResource myResource = new MyResource();
        try {
            myResource.doSomething();
            // first error 발생
        } finally {
            myResource.close();
            // second error 발생
        }
        // 추가적인 문제
        // 최초 발생한 first error를 먼저 보고 싶은데
        // 실행 해보면 second error 만 보여준다(마지막 에러)
        // 두번째 에러가 첫번째 에러를 잡아먹어 첫번째 에러가 안보인다.
        // 결국 디버깅이 굉장히 난감해진다.
    }
}
