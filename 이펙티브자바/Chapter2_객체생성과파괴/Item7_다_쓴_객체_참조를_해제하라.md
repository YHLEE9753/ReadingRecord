# Item7. 다 쓴 객체 참조를 해제하라

## 스터디 날짜

2022/03/31

## 1. 메모리 관리의 중요성

```java
// 코드 7-1 메모리 누수가 일어나는 위치는 어디인가? (36쪽)
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        // 이곳에서 예상하지 못한 메모리 누수!!!!!
        return elements[--size]; 
    }

    /**
     * 원소를 위한 공간을 적어도 하나 이상 확보한다.
     * 배열 크기를 늘려야 할 때마다 대략 두 배씩 늘린다.
     */
    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}
```
- 가비지 컬렉션 활동과 메모리 사용량이 늘어나 성능이 저하된다.
- 심한 경우 디스크 페이징이나 OutOfMemoryError 발생
- 이 코드에서는 스택에서 꺼내진 객체들을 가비지 컬렉터가 회수하지 않는다.
- 이 스택이 그 객체들의 다 쓴 참조(obsolete reference = 앞으로 다시 쓰지 않을 참조)를 여전히 가지고 있기 때문이다.
- elements 배열의 '활성 영역' 밖의 참조들이 모두 여기 해당된다.(활성 영역 : 인덱스가 size 보다 작은 원소들로 구성)


- 메모리 누수를 찾기 매우 어렵다.
- **객체 참조 하나를 살려두면 가비지 컬렉터는 그 객체뿐 아니라 그 객체가 참조하는 모든 객체(그리고 또 그 객체들이 참조하는 모든 객체 ..)를 회수해가지 못한다.**
- **단 몇개의 객체로 인해 많은 객체를 회수하지 못해 성능에 악영향 가능**




### 제대로 구현한 pop 메서드
```java
    // 코드 7-2 제대로 구현한 pop 메서드 (37쪽)
    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        Object result = elements[--size];
        elements[size] = null; // 다 쓴 참조 해제
        return result;
    }
```
- 해법은 해당 참조를 다 쓰면 null 처리(참조 해제)
- null 처리한 참조를 실수로 사용하려 하면 NullPointerException 발생(추가적 이점)
- 모든 객체를 다 쓰자마자 null 처리하는 것은 프로그램을 지저분하게 만들 뿐이다.
- 객체 참조를 null 처리하는 일은 예외적인 경우여야 한다.
- 다 쓴 참조를 해제하는 가장 좋은 방법은 그 참조를 담은 변수를 유효범위(scope) 밖으로 밀어내는 것이다.

## 2. null 처리는 언제 해야 할까?
- Stack 클래스는 자기 메모리를 직접 관리하기 때문에 메모리 누수에 취약하다.
- 스택은 객체 자체가 아닌 객체 참조를 담는 elements 배열로 저장소 풀을 만든다.
- 배열의 활성 영역에 속한 원소들이 사용되고 비활성 영역은 쓰이지 않는다.
  - 문제는 가비지 컬렉터는 이 사실을 아알 수가 없다.(둘다 똑같이 유효한 객체로 보인다)
- 프로그래머는 비활성 영역이 되는 순간 null 처리해서 GC 에게 알림을 준다.
- 즉 자기 메모리를 직접 관리하는 클래스라면 항시 메모리 누수에 주의하자

## 3. 추가적인 메모리 누수
### 캐시
캐시 역시 메모리 누수의 주범이다
- 객체 참조를 캐시에 넣고 까먹는 경우
- 캐시 외부에서 키를 참조하는 동안만 엔트리가 살아 있는 캐시가 필요한 경우라면 WeakHashMap 을 사용해 캐시를 만들자
- WeakHashMap 을 통해 다쓴 엔트리가 바로 삭제하게 할 수 있다(이경우에만 유효)


- 정확한 유효 시간을 알기 어렵기 때문에 시간이 지날수록 엔트리의 가치를 떨어뜨리는 방법도 쓴다.
- 이런경우 쓰지 않는 엔트리를 이따금 청소해주어야 한다. - 백그라운드 스레드 활용.

### 리스너(lister) 혹은 콜백(callback)
- 클라이언트가 콜백을 등록만 하고 명확히 해지하지 않는다면 콜백은 계속 쌓여 갈 것이다
- 콜백을 약한 참조(weak reference)로 저장하면 카비지 컬렉터가 즉시 수거해간다.
- 예로 WeakHashMap 에 키로 저장하면 된다.