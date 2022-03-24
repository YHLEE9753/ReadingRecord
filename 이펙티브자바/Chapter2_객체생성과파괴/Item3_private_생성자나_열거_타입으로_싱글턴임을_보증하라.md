# Item3. private 생성자나 열거 타입으로 싱글턴임을 보장하라

## 스터디 날짜
2022/03/26

## 1. 싱글턴(Singleton)
- 싱글턴이란 인스턴스를 오직 하나만 생성할 수 있는 클래스를 말한다.
- 예로는 함수와 같은 무상태 객체나 설계상 유일해야 하는 시스템 컴포넌트를 들 수 있다.

**클래스를 싱글텅으로 만들면 이를 사용하는 클라이언트를 테스트하기 어려워 질 수도 있다!!**
- 사용 클래스가 오직 1개이므로 테스트에 적합하지 않는다.

## 2. 싱글턴을 만드는 방식
### 1. public static 멤버가 final 필드인 방시
```java
public class YongHoon {
    public static final YongHoon INSTANCE = new YongHoon();
    private YongHoon() {}
}
```
- private 생성자는 public static final 필드를 초기화 할 때 딱 1번만 호출된다.
- public 이나 protected 생성자가 없으므로 인스턴스가 전체 시스템에서 1개뿐임을 보장한다.
- 리플렉션 API 사용시 private 생성자 호출 가능
  - 생성자를 수정하여 2번 째 객체가 생성 될려 할때 예외를 던지게 하면 방지가능

### 장점
- 해당 클래스가 싱글턴임이 API 에 명확히 드러난다.(public static final)
- 간결함

### 2. 정적 팩터리 메서드를 public static 멤버로 제공
```java
class YongHoon {
    private static final YongHoon INSTANCE = new YongHoon();
    private YongHoon() {}
    public static YongHoon getInstance() {
        return INSTANCE;
    }
}
```
- YongHoon.getInstance 는 항상 같은 객체의 참조를 반환한다. 
- 리플렉션을 통한 예외는 똑같이 적용

### 장점
- API를 변경하지 않아도싱글턴이 아니게 변경할 수 있다
- 정적 팩터리를 제네릭 싱글턴 팩터리로 만들 수 있다(item30)
- 정적 메서드 참조를 공급자(supplier) 로 사용 가능.

### 3. 열거 타입 방식의 싱글턴- 바람직한 방법
```java
enum YongHoon{
	INSTANCE;
    
}
```
- 간결하다
- 추가 노력없이 직렬화 가능
- 복잡한 직렬화 상황이나 리플렉션 공격에서 제 2의 인스턴스 생기는 일을 완벽히 막아준다.
- 단, 만들려는 싱글턴이 Enum 외의 클래스를 상속해야 한다면 이 방법은 사용 불가
  - 열거 타입이 다른 인터페이스를 구현하도록 선언할 수는 있다.