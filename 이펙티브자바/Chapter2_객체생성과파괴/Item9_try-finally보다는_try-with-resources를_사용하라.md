# Item9. try-finally 보다는 try-with-resources 를 사용하라

## 스터디 날짜

2022/04/05

## 1. 기본설정 코드

**FirstException.java**
```java
public class FirstException extends RuntimeException{
}
```

**SecondException.java**
```java
public class SecondException extends RuntimeException{
}
```

**MyResource.java**<br>
try with resource 를 사용하기 위해 AutoCloseable 을 implements 하였고, 내부 구현에서 위에서 만든 FirstException 과 SecondException 을 사용하였다.
```java

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

```

## 2. try finally
**case1**
```java
public class TestTryFinally {

    public static void main(String[] args) {
        // case1. 에러가 발생하여 close 하지 않는다.
        MyResource myResource = new MyResource();
        myResource.doSomething(); // 에러발생
        myResource.close(); // 실행되지 않는다.
    }
}
```

**case2.1**
```java
public class TestTryFinally {

    public static void main(String[] args) {
        // case2.1. try finally
        MyResource myResource = new MyResource();
        try {
            // 문제1 : 코드가 장황하다.
            myResource.doSomething();
            MyResource myResource1 = null;
            // 문제2 : nested 하게 try 를 쓰게 된다.
            try {
                myResource1 = new MyResource();
                myResource1.doSomething();
            } finally {
                if (myResource1 != null) {
                    myResource1.close();
                }
            }
        } finally {
            // 에러가 발생하든 말든 일단 close 시킨다.
            myResource.close();
        }

    }
}
```

**case2.2**
```java
public class TestTryFinally {

    public static void main(String[] args) {

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
```

## 3. try with resource

```java
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
```

### 결론 : 무조건 AutoCloseable 구현 후 try with resource 를 사용하자