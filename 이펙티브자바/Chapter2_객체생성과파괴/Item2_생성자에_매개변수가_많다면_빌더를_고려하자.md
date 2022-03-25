# Item2. 생성자에 매개변수가 많다면 빌더를 고려하자

## 스터디 날짜
2022/03/26

## 1. 점층적 생성자 패턴(telescoping constructor pattern)
필수 매개변수만 받는 생성자, 필수 매개변수와 선택 매개변수 1개를 받는 생성자. ... 2개 .. 3개 ...<br>
**필요한 모든 경우에 대해 생성자를 미리 만들어두어야 한다**

### 점층적 생성자 패턴의 단점
- 매개변수 개수가 많아지면 클라이언트 코드를 작성하거나 읽기 어렵다.
  - 코드를 읽을 떄 각 값의 의미가 무엇인지 헷갈린다
  - 매개변수 갯수도 세어보아야 한다
  - 타입이 같은 매개변수가 연달아 늘어서 있으면 버그로 이어진다
  - 실수로 매개변수 순서를 바꿔주어도 컴파일러에서 알아차리지 못할 수도 있다.

```java
NutritionFacts cocaCola = new NutritionFacts(240, 8, 100, 0, 35, 27); 
```

## 2. 자바빈즈 패턴(JavaBeans pattern)
매개변수가 없는 생성자로 객체를 만든후 세터 메서들을 호출해 매개변수의 값을 설정하는 방식<br>

점층적 생성자 패턴과 비교해 코드는 길어지지만 인스턴스를 만들기 쉽고, 더 읽기 쉬운 코드가 되었다.

### 자바빈즈 패턴의 단점
- 객체 하나를 만들려면 메서드를 여러 개 호출해야 한다.
- 객체가 완전히 생성되기 전까지는 일관성(consistency)이 무너진 상태에 놓이게 된다.
  - 일관성이 깨진 객체의 경우 런타임이나 컴파일에서 잡히지 않아 디버깅에 큰 어려움을 겪을 수 있다.
  - 따라서 클래스를 불변으로 만들 수 없다
- **절대 사용하지 말아라**

## 3. 빌더 패턴
- 점층적 생성자 패턴의 안전성과 자바비즈 패턴의 가독성을 가지고 있다.
- 필수 매개변수만으로 생성자를 호출해 빌더 객체를 얻는다.
- 그 다음 내부 메서드들을 통해 원하는 선택 매개변수들을 선정한다.
- 마지막으로 매개변수가 없는 build 메서드를 호출해 (보통은 불변) 객체를 얻는다.
- 빌더는 생성할 클래스 안에 정적 멤버 클래스로 만들어 두는 게 보통이다

```java
package EffectiveJava.chapter2.item2;

public class NutrionFacts {
  private final int servingSize;
  private final int servings;
  private final int calories;
  private final int fat;
  private final int sodium;
  private final int carbohydrate;

  public static class Builder{
    // 필수 매개변수
    private final int servingSize;
    private final int servings;

    // 선택 매개변수 - 기본값으로 초기화한다.
    private int calories = 0;
    private int fat = 0;
    private int sodium = 0;
    private int carbohydrate = 0;

    public Builder(int servingSize, int servings) {
      this.servingSize = servingSize;
      this.servings = servings;
    }

    public Builder calories(int val){
      calories = val;
      return this;
    }
    public Builder fat(int val){
      fat = val;
      return this;
    }
    public Builder sodium(int val){
      sodium = val;
      return this;
    }
    public Builder carbohydrate(int val){
      carbohydrate = val;
      return this;
    }

    public NutrionFacts build(){
      return new NutrionFacts(this);
    }
  }

  private NutrionFacts(Builder builder) {
    this.servingSize = builder.servingSize;
    this.servings = builder.servings;
    this.calories = builder.calories;
    this.fat = builder.fat;
    this.sodium = builder.sodium;
    this.carbohydrate = builder.carbohydrate;
  }

  // 실행
  public static void main(String[] args) {
    NutrionFacts nutrionFacts = new NutrionFacts
            .Builder(240,8)
            .calories(100)
            .sodium(35)
            .carbohydrate(27)
            .build();
  }
}
```

- NutrionFacts 클래스는 불변이며, 세터 메서드들은 빌더 자신을 반환하기 때문에 연쇄적으로 호출 가능하다.
- 이러한 호출 발식을 플루언트 API(fluent API) 혹은 메서드 연쇄(method chaining) 라 한다.
- 다만 유효성 검사 코드가 생략되어 있다.
  - builder 메서드가 호출하는 생성자에서 여러 매개변수에 걸친 불변식(invariant)를 검사하자.
  - 공격에 대비해 불변식을 보장하기 위해 빌더로부터 매개변수를 복사한 후 해당 객체 피드들도 검사하자
  - 검사 후 문제 발생시 IllegalArgumentException 을 던지자

### 참고 불변과 가변
불변(immutable) : 어떠한 변경도 허용하지 않는다. 대표적으로 String 객체<br>
불변식(invariant) : 프로그램 실행 동안, 반드시 만족해야 하는 조건
- 예) 리스트의 크기는 반드시 0보다 크다.

## 4. 계층적으로 설계된 클래스와 빌더 패턴
**Pizza**
```java
package EffectiveJava.chapter2.item2;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public abstract class Pizza {
    public enum Topping{HAM, MUSHROOM, ONION, PEPPER, SAUSAGE} // Toppind enum
    final Set<Topping> toppings; // set 으로 설정

    abstract static class Builder<T extends Builder<T>>{
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class); // EnumSet 생성 메서드
        public T addTopping(Topping topping){ //addTopping 함수 선언시 set 에 입력값 추가
            toppings.add(Objects.requireNonNull(topping));
            return self(); // T 반환
        }
        abstract Pizza build(); // overriding 필요

        // 하위 클래스는 이 메서드를 재정의 하여
        // this 를 반환하도록 해야 한다.
        protected abstract T self(); // overriding 필요
    }

    // Pizza 생성자
   Pizza(Builder<?> builder){
        toppings = builder.toppings.clone();
    }
}
```

**NyPizza**
```java
package EffectiveJava.chapter2.item2;

import java.util.Objects;

import static EffectiveJava.chapter2.item2.Pizza.Topping.ONION;

public class NyPizza extends Pizza{
    public enum Size {SMALL, MEDIUM, LARGE}; // 사이즈 enum
    private final Size size; // 나의 크기 필수값

    public static class Builder extends Pizza.Builder<Builder>{ // 피자.Builder 을 상속
        private final Size size;

        public Builder(Size size) {
            // 나의 크기 설정(필수값)
            this.size = Objects.requireNonNull(size);
        }

        @Override
        public NyPizza build() {
            // 피자 반환
            return new NyPizza(this);
        }
        @Override
        protected Builder self() {
            return this;
        }
    }

    private NyPizza(Builder builder) {
        super(builder);
        size = builder.size;
    }

    // 실행
    public static void main(String[] args) {
        NyPizza nyPizza = new NyPizza
                .Builder(Size.SMALL)
                .addTopping(ONION)
                .build();
    }
}
```
- 뉴욕피자는 크기(size) 매개변수를 필수로 받는다.
- NyPizza.Builder 는 NyPizza 를 반환한다.
- 괸장히 유연하게 사용 가능하다

## 5. 빌더 패턴의 단점
- 빌더 생성 비용이 크지는 않지만 성능에 민감한 상황의 경우 문제가 될 수 있다.
- 코드가 길기 때문에 매개변수가 4개 이상일 때 값어치를 한다.
- **하지만 이점이 많기 때문에 빌더 패턴을 애용하자**

## 6. Lombok 빌더
### 클래스에 @Builder 사용할 경우
- ```@Builder``` : Builder 패턴을 자동으로 생성해주는데, builderMethodName에 들어간 이름으로 빌더 메서드를 생성해준다.<br>
- ```@Builder``` 어노테이션을 클래스에 작성할 경우 반드시 ```@AllArgsConstructor``` 를 적어주어야 한다.
- ```@Builder.Default``` : JPA 엔티티 생성시 1대다 관계를 위해 미리 배열을 담든 경우가 있는데 이런경우에 어노테이션을 쓰면 null 값 초기화에서 그 변수를 제외한다.
- 클래스 내부 builder 메서드 : 필수로 들어가야할 필드들을 검증하기 위해 만들었다. 해당 클래스를 객체로 생성할 때 필수적인 필드가 있다면 활용할 수 있다.


```java
@Entity
@Getter
@Builder
@Table(name = "MywDrug")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MyDrug {

    @Id
    @Column(name = "mydrug_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // n:1 매핑
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "myDrug") // 1:다 매핑
    @Builder.Default
    private List<MyDrugDetail> myDrugDetails = new ArrayList<>();

    // 투약정보 type1
    @Column(name = "treat_type")
    private String treatType; // 진료형태

    @Column(name = "visit_count")
    private Long visitCnt; // 방문일수

    @Column(name = "treat_dsnm")
    private String treatDsnm; // 진료대상자명

    @Column(name = "treat_date")
    private Long treatDate; // 진료개시일

    @Column(name = "medicine_count")
    private Long medicineCnt; // 투약(요양)횟수

    @Column(name = "treat_dsgb")
    private String treatdsgb; // 1:본인, 2:자녀

    @Column(name = "prescribe_cnt")
    private Long prescribeCnt; // 처방 횟수

    @Column(name = "treat_medicalnm")
    private String treatMedicalnm; //병의원(약국)명

    public void changeUser(User user) {
        this.user = user;
        user.getMyDrugs().add(this);
    }


    // 필수 매개변수 체크 용 클래스 내부 builder 메서드
    public static TravelCheckListBuilder builder(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("필수 파라미터 누락");
        }
        return travelCheckListBuilder().id(id);
    }
}

```

```java
MyDrug myDrug=MyDrug.builder()
        .treatType(caseType)
        .visitCnt(visitcnt)
        .treatDsnm(treatdsnm)
        .treatDate(treatdate)
        .medicineCnt(medicinecnt)
        .treatdsgb(treatdsgb)
        .prescribeCnt(prescribecnt)
        .treatMedicalnm(treatmedicalnm)
//        .myDrugDetails(new ArrayList<>())
        .build();
```

### 생성자에 @Builder 를 사용할 경우
- lombok 사용 시 생성자 매개변수의 순서가 바뀌어 에러가 발생할 수 있으므로 ```@AllArgsConstructor``` 사용을 지양해야 한다.
- 클래스 상단에 선언하면 비즈니스 로직에서 어떻게 객체를 생성하는지 추척이 어렵다.
- 개발자의 실수로 필드를 누락하여 null 이 들어갈 수 있고 이러한 실수는 런타임때 발견이 된다.
- 따라서 정말 간단한 객체가 아니라면 생성자를 통한 빌더 사용을 권장한다.

```java
public static class Order {
    private long cancelPrice;
    private long orderPrice;
 
    @Builder
    private Order(long cancelPrice, long orderPrice) {
        this.cancelPrice = cancelPrice;
        this.orderPrice = orderPrice;
    }
}
```
