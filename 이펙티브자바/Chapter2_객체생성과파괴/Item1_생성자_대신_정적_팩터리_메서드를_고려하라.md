# Item1. 생성자 대신 정적 팩터리 메서드를 고려하라

## 스터디 날짜

2022/03/26

## 1. 정적 팩터리 메서드(Static Factory Method)란?

- 클래스의 인스턴스를 얻는 전통적인 수단은 public 생성자이다.
- 정적 팩터리 메서드는 객체 생성의 역할을 하는 클래스 메서드로 클래스의 인스턴스를 반환하는 단순한 정적 메서드이다.

정적 팩터리 메서드의 예시<br>
java.time 패키지에 포함된 LocalTime 클래스의 정적 팩토리 메서드

```java
// LocalTime.class
...
public static LocalTime of(int hour,int minute){
        ChronoField.HOUR_OF_DAY.checkValidValue((long)hour);
        if(minute==0){
        return HOURS[hour];
        }else{
        ChronoField.MINUTE_OF_HOUR.checkValidValue((long)minute);
        return new LocalTime(hour,minute,0,0);
        }
        }
        ...

// hour, minutes을 인자로 받아서 9시 30분을 의미하는 LocalTime 객체를 반환한다.
        LocalTime openTime=LocalTime.of(9,30);
```

## 2. 정적 메서드의 장점

### 1. 이름을 가질 수 있다.

- 생성자는 반환할 객체의 특성을 제대로 설명하기 어렵다
- 정적 팩터리는 이름만 잘 지으면 반환할 객체의 특성을 쉽게 묘사할 수 있다.
- 정적 팩토리 메서드를 사용하면 해당 생성의 목적을 이름에 표현할 수 있어 가독성이 좋아지는 효과가 있다.

```BigInteger.probablePrime```

### 2. 호출될 때마다 인스턴스를 새로 생성하지는 않아도 된다.

- 불변 클래스는 인스턴스를 미리 만들어 놓을 수 있다.
- 새로 생성한 인스턴스를 캐싱하여 재활용할 수 있다.
- 위와 같이 불필요한 객체 생성을 피할 수 있다.
- 심지어 객체를 아예 생성하지 않는 메서드도 있다. ```Boolean.valueOf(boolean)```
- (특히 생성 비용이 큰)같은 객체가 자주 요청되는 상황이라면 성능을 상당히 끌어올려 준다.

- 반복되는 요청에 같은 객체를 반환하는 형식은 언제 어느 인스턴스를 살아 있게 할지를 통제 할 수 있다.
    - 이러한 클래스를 인스턴스 통제(instance-controlled) 클래스라 한다.
    - 통제 이유
        1. 클래스를 싱글턴으로 변경 가능
        2. 인스턴스화 불가로 변경 가능
        3. 불변 값 클래스에서 동치인 인스턴스가 단 1개뿐임을 보장
        4. 인스턴스 통제는 플라이웨이터 패턴의 근간이 된다.
        5. 열거타입은 인스턴스가 1개만 만들어짐을 보장한다.

```java
class Singleton {
    private static Singleton singleton = null;

    private Singleton() {
    }

    static Singleton getInstance() {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }
}

```

- 정적 팩토리 메서드는 기본적으로 객체 생성을 책임지고 있기 때문에 객체의 생성을 관리한다고도 할 수 있습니다.
- 즉 필요에 따라 항상 새로운 객체를 생성해서 반환할 수도 있고, 하나의 객체만 계속 반환할 수도 있습니다.
- 객체 생성을 관리할 수 있다는 이야기는 불필요한 객체를 만들지 않을 수 있다는 뜻을 내포하고 있습니다.
- 싱글톤(singleton)을 예제로 설명한다면, getInstance(정적 팩토리 메서드)를 사용해 하나의 객체만 사용하게 만든 부분이 객체 생성을 관리한다고 이야기해볼 수 있습니다.

출처: https://7942yongdae.tistory.com/147 [프로그래머 YD]

### 2.1 플라이웨이트 패턴(Flyweight pattern)

- 어떤 클래스의 인스턴스 1개만 가지고 여러 개의 "가상 인스턴스"를 제공하고 싶을 떄 사용하는 패턴이다.
- 즉 인스턴스를 가능한 대로 공유시켜 쓸데없이 ```new``` 연산자를 통한 메모리 낭비를 줄이는 방식이다.

출처: https://lee1535.tistory.com/106

### 3. 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다..

- 정적 팩토리 메서드는 객체 생성만 할 뿐 구현부는 외부에 보여주지 않는 특징이 있습니다.
- 노출하지 않는다는 특징은 은닉성을 가지기도 하지만 동시에 사용하고 있는 구현체를 숨겨 의존성을 제거해줍니다.

```java
public class Application {
    public static void main(String[] args) {
        System.out.println(GradeCalculator.of(88).toText());
        System.out.println(GradeCalculator.of(50).toText());
    }
}

class GradeCalculator {
    static Grade of(int score) {
        if (score >= 90) {
            return new A();
        }
        if (score >= 80) {
            return new B();
        }
        return new F();
    }
}

interface Grade {
    String toText();
}

class A implements Grade {
    @Override
    public String toText() {
        return "A";
    }
}

class B implements Grade {
    @Override
    public String toText() {
        return "B";
    }
}

class F implements Grade {
    @Override
    public String toText() {
        return "F";
    }
}

```

- Grade를 사용하는 main()에서는 Grade 인터페이스를 사용함으로써 구현체인 A, B, F를 몰라도 전혀 문제가 없습니다.
- 구현체를 알고 생성해서 반환할 책임은 오롯이 정적 팩토리 메서드를 가진 GradeCalculator입니다.

출처: https://7942yongdae.tistory.com/147 [프로그래머 YD]

### 4. 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.

- 반환 타입의 하위 타입이기만 하면 어떤 클래스의 객체를 반환하든 상관없다.
- 클라이언트는 팩터리가 건네주는 객체가 어느 클래스의 인스턴스인지 알 수도 없고 알 필요도 없다.
- 구체 클래스에 대해 알 필요가 없기때문에 책임분리가 명확하고 캡슐화가 잘 진행되어있다.

```java
Car carDto=CarDto.from(car); // 정적 팩토리 메서드를 쓴 경우
        CarDto carDto=new CarDto(car.getName(),car.getPosition); // 생성자를 쓴 경우
```

만약 정적 팩토리 메서드를 쓰지 않우면 외부에서 생성자의 내부 구현을 모두 드러낸 채 해야할 것이다.

### 5. 정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.

**서비스 제공자 프레임워크**

- JDBC(Java Database Connectivity API)와 같은 서비스 제공자 프레임워크의 근간을 이루는 것이 바로 유연한 성격을 지닌 정적 팩토리 메소드들이다.
- 서비스 제공자 프레임워크는 다양한 서비스 제공자들이 하나의 서비스를 구성하는 시스템으로, 클라이언트가 실제 구현된 서비스를 이용할 수 있도록 하는데, 클라이언트는 세부적인 구현 내용을 몰라도 서비스를 이용할 수
  있다.
- 자바의 JDBC는 MySQL, Oracle, SqlServer 등 다양한 서비스 제공자들이 JDBC라는 하나의 서비스를 구성한다.

**구현체들을 클라이언트에 제공하는 역할을 프레임워크가 통제하여, 클라이언트를 구현체로부터 분리해 준다.**


서비스 제공자 프레임워크는 세 가지의 핵심 컴포넌트로 구성된다.

(1) 서비스 제공자가 구현하는 서비스 인터페이스<br>
(2) 구현체를 시스템에 등록하여 클라이언트가 쓸 수 있도록 하는 서비스 등록 API<br>
(3) 클라이언트에게 실제 서비스 구현체를 제공하는 서비스 접근 API<br>

```java
        String driverName="com.mysql.jdbc.Driver";
        String url="jdbc:mysql://localhost:3306/board";
        String user="root";
        String password="1234@";

        try{
        Class.forName(driverName);

        // 서비스 접근 API인 DriverManager.getConnection
        // 서비스 구현체(서비스 인터페이스)인 Connection를 반환한다.
        Connection conn=DriverManager.getConnection(url,user,password);


        }catch(ClassNotFoundException e){
        e.printStackTrace();
        }catch(SQLException e){
        e.printStackTrace();
        }


```
- 인터페이스와 실제 그 인터페이스를 구현하는 구현체 클래스가 완전히 분리되어 제공된다는 것이다. 
- 인터페이스를 사용해 틀을 만들어 놓고 그 틀에 맞춰 각각의 서비스 제공자들이 자신의 서비스에 맞는 구현 클래스를 제공하도록 하는 것이다.

출처: https://plposer.tistory.com/61 [안JAVA먹지]


## 3. 정적 메서드의 단점
### 1. 상속을 하려면 public 이나 protect 생성자가 필요하니 정적 팩터리 메서드만 제공하면 하위 클래스를 만들 수 없다.
- 앞서 이야기한 구현 클래스들을 상속할 수 없다는 이야기다.
- 이 제약은 상속보다 컴포지션을 사용하도록 유도하고 불변 타입으로 만들려면 이 제약을 지켜야 한다는 점에서 오히려 장점으로 받아들일 수도 있다.


### 2. 정적 팩터리 메서드는 프로그래머가 찾기 어렵다

## 4. 네이밍 컨벤션
정적 팩토리에서 흔히 사용하는 명명 방식.

|명명 규칙|설명|
|-----|---|
|from|매개변수를 하나 받아서 해당 타입의 인스턴스를 반환하는 형변환 메소드.|
|of|여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메소드.|
|valueOf|from 과 of 의 더 자세한 버전|
|instance or getInstance|(매개 변수를 받는다면) 매개변수로 명시한 인스턴스를 반환하지만 같은 인스턴스임을 보장하지는 않는다.|
|create or newInstance|instance 혹은 getInstance와 같지만 매번 새로운 인스턴스를 생성해 봔환함을 보장한다.|
|getType|getInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩토리 메소드를 정의할 때 쓴다. |
|newType|newInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩토리 메소드를 정의할 때 쓴다.|
|type|getType과 newType의 간결한 버전|
