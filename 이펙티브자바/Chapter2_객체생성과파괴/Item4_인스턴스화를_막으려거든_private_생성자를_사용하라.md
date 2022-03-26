# Item4. 인스턴스화를 막으려거든 private 생성자를 사용하라

## 스터디 날짜
2022/03/29

## 1. 정적 메서드와 정적 필드만을 담은 클래스
- java.lang.Math, java.util.Arrays 처럼 기본 타입 값이나 배열 관련 메서드들을 모아 놓는 경우
- java.util.Collections 처럼 특정 인터페이스를 구현하는 객체를 생성해주는 정적 메서드(혹은 팩터리)를 모아 놓는 경우
- final 클래스와 관련 메서드들을 모아 놓는 경우
  - final 클래스를 상속해서 하위 클래스에 메서드를 넣는 건 불가능하기 때문(final 클래스는 상속을 막고, final 메서드는 override 를 막는다)
  - https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=highkrs&logNo=220213143536

## 2. 정적 멤버만 담은 유틸리티 클래스는 인스턴스로 만들어 쓰려고 설계한 것이 아니다. 
- 생성자를 명시하지 않으면 컴파일러가 자동으로 기본 생성자를 만들어 준다.(의도치 않은 설계 가능)
- 추상 클래스로 만드는 것으로 인스턴스화를 막을 수 없다.
  - 하위 클래스를 만들어 인스턴스화 하면 된다
  - 사용자가 상속해서 쓰라고 오해 할 수 있다.

## 3. 인스턴스화 막는 방법
- private 생성자를 추가하면 클래스의 인스턴스화를 막을 수 있다.
```java
public class UtilityClass{
    // 기본 생성자가 만들어지는 것을 막는다(인스턴스화 방지용).
    private UtilityClass(){
        throw new AssertionError();
    }

}
```
- AssertionError 사용을 통해 실수로라도 클래스 안에서 생성자 호출되는 경우를 막아준다.
- 이 방식은 상속을 불가능하게 하는 효과가 있다.
  - 모든 생성자는 상위 클래스의 생성자를 호출하게 되는데, 이를 private 으로 선언했기 때문에 하위 클래스가 상위 클래스의 생성자에 접근할 길이 막힌다.
