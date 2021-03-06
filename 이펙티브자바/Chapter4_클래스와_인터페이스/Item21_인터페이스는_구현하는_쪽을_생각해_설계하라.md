# Item21. 인터페이스는 구현하는 쪽을 생각해 설계하라

## 스터디 날짜

2022/05/13

## 1. 인터페이스
자바 8 이전에는 기존 구현체를 깨뜨리지 않고는 인터페이스에 메서드를 추가할 방법이 없었다.
- 인터페이스에 메서드를 추가하면 보통은 컴파일 오류가 나는데, 추가된 메서드가 기존 구현체에 존재할 가능성이 아주 낮기 때문이다.

자바 8 이후부터 기존 인터페이스에 메서드를 추가할 수 있도록 디폴트 메서드가 추가되었다.
- 디폴트 메서드를 선언하면, 그 인터페이스를 구현한 후 디폴트 메서드를 재정의하지 않은 모든 클래스에서 디폴트 구현이 쓰이게 된다.
- 디폴트 메서드는 구현 클래스에 대해 아무것도 모른 채 합의 없이 무작정 '삽입'될 뿐이므로 주의해야 한다.

자바 8에서는 핵심 컬렉션 인터페이스들에 다수의 디폴트 메서드가 추가되었다. 이는 주로 람다를 활용하기 위해서다.
- 자바 라이브러리의 디폴트 메서드는 코드 품질이 높고 범용적이라 대부분의 상황에서 잘 작동하지만
- **생각할 수 있는 모든 상황에서 불변식을 해치지 않는 디폴트 메서드를 작성하는 것은 어렵다.**

> 불변식 : 한번 만들어진 식(객체)이 변하지 않다는 것을 의미

**자바 8의 Collection 인터페이스에 추가된 디폴트 메서드**
```java
public interface Collection<E> extends Iterable<E> {
    default boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }
}
```
- 이 메서드는 주어진 boolean 함수(predecate)가 true를 반환하는 모든 원소를 제거한다.
- 위 코드는 범용적으로 구현되었지만 현존하는 모든 Collection 구현체와 잘 어우러지는 것은 아니다.
- SynchronizedCollection이 대표적인 예다.
  - 아파치 버전은 클라이언트가 제공한 객체로 락을 거는 기능을 추가로 제공한다.
  - 즉, 모든 메서드에서 주어진 락 객체로 동기화한 후 내부 컬렉션 객체에 기능을 위임하는 래퍼 클래스다.
  - 따라서 SynchronizedCollection 인스턴스를 여러 스레드가 공유하는 환경에서 한 스레드가 removeIf를 호출하면 concurrentModificationException이 발생하거나 다른 예기치 못한 결과로 이어질 수 있다.

**디폴트 메서드가 추가된것을 인지 하지 못하고 removeIf 메서드를 재정의 하지 않는다면 오류가 발생하거나 예기치 못한 결과로 이어질 수 있다.**

## 2. 디폴트 메서드 호환성을 유지하기 위한 방법
- 구현한 인터페이스의 디폴트 메서드를 재정의
- 다른 메서드 에서는 디폴트 메서드를 호출하기 전에 필요한 작업을 수행하도록 했다.

Collections.synchronizedCollection이 반환하는 package-private 클래스 들은 removeIf를 재정의하고, 이를 호출하는 다른 메서드들은 디폴트 구현을 호출하기 전에 동기화를 하도록 했다.
<br>
4.4 버전 이후부터는 override 즉 재정의 되어있다.
```java
public class SynchronizedCollection<E> implements Collection<E>, Serializable {
    ...

    /**
    * @since 4.4
    */
    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        synchronized (lock) {
            return decorated().removeIf(filter);
        }
    }
}
```

- **디폴트 메서드는 (컴파일에 성공하더라도) 기존 구현체에 런타임 오류를 일으킬 수 있다.**

### 꼭 필요한 경우가 아니면 디폴트 메서드를 추가하는 것은 피하자
기존 메서드를 제거하거나 수정하는 용도가 아니다.<br>
디폴트 메서드로 인해 기존 클라이언트를 망가뜨릴 수 있다.<br>

따라서, 인터페이스를 설계 할 때는 여전히 세심한 주의를 기울여야 한다.<br>
이를 검증하기 위해 서로 다른 방식으로 최소 세 가지의 구현체를 만들어 보자.<br>

**인터페이스를 릴리즈한 후라도 결함을 수정하는 게 가능한 경우도 있지만, 이를 보험삼아서는 안된다.**

## 3. 핵심
1. 디폴트 메서드 사용은 불변식을 보장하지 못한다.
2. 디폴트 메서드가 추가된것을 인지 하지 못하고 메서드를 재정의 하지 않는다면 오류가 발생하거나 예기치 못한 결과로 이어질 수 있다.
3. 따라서 디폴트 메서드는 (컴파일에 성공하더라도) 기존 구현체에 런타임 오류를 일으킬 수 있다.
4. 결론적으로 꼭 필요한 경우가 아니면 디폴트 메서드를 추가하지 말자
   1. 디폴트 메서드로 인해 기존 클라이언트를 망가뜨릴 수 있다.
5. 또한 인터페이스 설계에 세심한 주의를 기울이고, 항상 여러개의 구현체를 통해 테스트를 진행하자.

## 4. 디폴트 메서드를 사용하는 이유
[오브젝트 600p]<br>
디폴트 메서드가 추가된 이유 :
- 기존에 널리 사용되고 있는 인터페이스에 새로운 오퍼레이션을 추가할 경우 하위 호환성 문제를 해결하기 위해서이다.

오해1. 추상클래스의 역할을 대체하기 위해 디폴트 메서드를 사용하는것인가? No<br>
```java
public interface DiscountPolicy {
    default int calculateDiscountAmount(Object movie){
        for(String each : getConditions()){
            if(each.equals((String) movie)){
                return getDiscountAmount(movie);
            }
        }
        return 0;
    }

    List<String> getConditions(); // 디폴트 메서드 내부 구현
    int getDiscountAmount(Object movie); // 디폴트 메서드 내부 구현
    String publicInterface(); // 퍼블릭 인터페이스
}


```
```java

public class AmountDiscountPolicy implements DiscountPolicy{
    @Override
    public List<String> getConditions() { // 내부 구현에 사용되는 메서드 접근자가 public 으로 열린다.
        return null;
    }

    @Override
    public int getDiscountAmount(Object movie) { // 내부 구현에 사용되는 메서드 접근자가 public 으로 열린다.
        return 0;
    }

    @Override
    public String publicInterface() {
        return null;
    }
}

```
- 캡슐화를 약화시킨다.
- 인터페이스가 불필요하게 비대해진다.
- 코드 중복을 환벽하게 제거하지 못한다.