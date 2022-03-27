# Item5. 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

## 스터디 날짜
2022/03/29

## 1. 정리
- 클래스가 내부적으로 1개 이상의 자우너에 의존하고, 그 자원이 클래스 동작에 영향을 준다면 싱글턴과 정적 유틸리티 클래스는 사용하지 않는 것이 좋다.
- 이 자원들을 클래스가 직접 만들게 해서도 안된다. 대신 필요한 자원을(혹은 그 자원을 만들어주는 팩터리를) 생성자에 (혹은 정적 팩터리나 빌더에) 넘겨주자
- 의존 객체 주입이라 하는 이 기법은 클래스의 유영성, 재 사용성, 테스트 용이성을 기막히게 개선해준다.

## 2. 안좋은 사례
- 많은 클래스가 1개 이상의 자원에 의존한다.

```java
public class SpellChecker{
    private static final Lexicon dictionary = ...;
    
    private SpellChecker() {} // 객체 생성 방지
    
    public static boolean isValid(String word) { ... }
    public static List<String> suggestions(STring typo) { ... }
}
```
- 위 코드에서 final 한정자를 제거하고 다른 사전으로 교체하는 메서드를 추가할 수 있다. 하지만 좋은 방식은 아니다
  - 오류를 내기 쉽다
  - 멀티스레드 환경에서는 쓸 수 없다.
  - 유연하지 않으며 테스트 하기 어렵다.
- 사용하는 자원에 따라 동작이 달라지는 클래스에는 정적 유틸리티 클래스나 싱글턴 방식이 적합하지 않다.

- 위 코드에서 dictionary 는 static 객체이며 사용 자원에 많아지면 불변 객체가 아니게 된다.
- 의존 객체 주입 패턴을 사용하자

## 3. 의존 객체 주입 패턴
```java
public class SpellChecker{
    private final Lexicon dictionary;
    
    private SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }
    
    public static boolean isValid(String word) { ... }
    public static List<String> suggestions(STring typo) { ... }
}
```
- 자원이 몇개든 의존 관계가 어떻든 상관없이 잘 작동한다.
- 불변을 보장하여 (같은 자원을 사용하려는) 여러 클라이언트가 의존 객체들을 안심하고 공유할 수 있다.
- 생성자, 정적 팩터리, 빌더 모두에 똑같이 응용할 수 있다.

## 4. 변형 - 생성자에 자원 팩터리를 넘겨주는 방식
팩터리 : 호출할 때마다 특정 타입의 인스턴스를 반복적으로 만들어주는 객체<br>
팩터리 메서드 패턴(Factory Method Pattern)의 구현이다.<br>
자바 8의 ```Supplier<T>``` 인터페이스가 팩터리를 표현한 완벽한 예이다.
```java
Mosaic create(Supplier<? extends Tile> tileFactory) { ... }
```


## 5. 의존 객체 주입의 한계와 개선
- 유연성과 테스트 용이성을 개선하지만, 의존성이 수천개나 되는 큰 프로젝트에서는 코드를 어지럽게 만들기도 한다.
- 스프링 같은 객체 주입 프레임워크를 통해 해소시킬 수 있다,
