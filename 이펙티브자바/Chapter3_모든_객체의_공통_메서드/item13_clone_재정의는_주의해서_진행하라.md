# Item13. clone 재정의는 주의해서 진행하라

## 스터디 날짜

2022/04/12

## 1. clone 의 차이점

- Cloneable 은 복제해도 되는 클래스임을 명시하는 용도의 믹스인 인터페이스이다.
- clone 메서드가 선언된 곳이 Cloneable 이 아닌 Object 이다
    - 그마저도 protected 이다
- 따라서 Cloneable 을 구현하는 것만으로는 외부 객체에서 clone 메서드를 호출할 수 없다.
- 리플렉션을 사용하면 100% 성공하는 것도 아니다.
    - 해당 객체가 접근이 허용된 clone 메서드를 제공한다는 보장이 없기 때문

```java
public class Object {
    @IntrinsicCandidate //(해당 어노테이션을 수정할 때 HotSpot VM도 또한 같이 업데이트를 해줘야 한다.)
    protected native Object clone() throws CloneNotSupportedException;
    // C, C++ 등의 언어로 작성된 native 이다
    ...
}
```

```java
public interface Cloneable {
}
```

- Cloneable 인터페이스는 Object의 protected 메서드인 clone 의 동작 방식을 결정한다.<br>
- 인터페이스를 구현하는 것은 해당 클래스가 그 인터페이스에서 정의한 기능을 제공한다는 의미이지만, Cloneable 의 경우 상위 클래스에 정의된 protected 메서드의 동작 방식을 변경한 것이다.
- 실무에서 public 으로 clone 메서드 제공 시 생성자를 호출하지 않고도 객체를 생성할 수 있는 위험한 상ㅇ황이 가능하다.

### Object 명세의 clone 메서드 규약

> 이 객체의 복사본을 생성해 반환한다. '복사'의 정확한 뜻은 그 객체를 구현한 클래스에 따라 다를 수 있다. 일반적인 의도는 다음과 같다. 어떤 객체 x에 대해 다음 식은 참이다.<br>  x.clone() != x

> 또한 다음 식도 참이다.<br>x.clone().getClass() == x.getClass()

> 하지만 이상의 요구를 반드시 만족해야 하는 것은 아니다. 한편 다음 식도 일반적으로 참이지만, 역시 필수는 아니다.<br>x.clone().equals(x)

> 관례상, 이 메서드가 반환하는 객체는 super.clone을 호출해 얻어야 한다. 이 클래스와 (Object를 제외한) 모든 상위 클래스가 이 관례를 따른다면 다음 식은 참이다.<br>x.clone().getClass() == x.getClass()

> 관례상, 반환된 객체와 원본 객체는 독립적이어야 한다. 이를 만족하려면 super.clone으로 얻은 객체의 필드 중 하나 이상을 반환 전에 수정해야 할 수도 있다.

## 2. 제대로된 clone 을 만들어 보자

- super.clone 을 호출
- 클래스에 정의된 모든 필드는 원본 필드와 똑같은 값
- 모든 필드가 기본타입이거나 불변 객체를 참조한다면 완벽하다.
    - 하지만 쓸데없는 복사를 지양한다는 관점에서 불변 클래스는 굳이 clone 메서드를 제공하지 않는 것이 좋다
      **가변 상태를 참조하지 않는 클래스용 clone 메서드**

```java
    @Override
    public PhoneNumber clone(){
        try{
            return(PhoneNumber)super.clone();
        }catch(CloneNotSupportedException e){
            throw new AssertionError();
        }
    }
```
- 공변 반환 타입(covariant return typing)
  - 메서드가 오버라이딩될 때 더 좁은(narrower) 타입으로 교체할 수 있다는 것

이와 같이 불변 클래스는 Cloneable 을 구현할 필요가 없기때문에 Cloneable 을 implements 하지 않고 clone 을 override 한 후 CloneNotSupportedException 으로 잡아준다.


https://www.pixelstech.net/article/1420629927-What-does-super-clone%28%29-do



