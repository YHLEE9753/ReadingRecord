# Item6. 불필요한 객체 생성을 피하라

## 스터디 날짜

2022/03/31

## 1. 객체 재사용

- 재사용은 빠르고 세련되다
- 불변 객체는 언제나 재사용 가능하다

```java
String s=new String("yong");
        String s2=new String("yong");
        System.out.plintln(s==s2); // false 반환
```

위 코드는 문장 생성시 마다 String 인스턴스를 만든다. String 인스턴스가 수백만 개 만들어질 수도 있다.

```java
String s="yong";
        String s2="yong";
        System.out.plintln(s==s2); // true 반환
```

새로운 인스턴스를 매번 만드는 대신 하나의 String 인스턴스를 사용한다. 가상머신안에서 똑같은 문자열 리터럴을 사용하는 모든 코드가 같은 객체를 재사용함이 보장된다.

## 2. 정적 팩터리 메서드를 제공하는 불변 클래스의 정적 팩터리 메서드를 통해 불필요한 객체 생성 회피

```java
public class Main {
    public static void main(String[] args) {
        Boolean true1 = Boolean.valueOf("true");
        Boolean true2 = Boolean.valueOf("true");

        System.out.println(true1 == true2); //true 반환
        System.out.println(Boolean.TRUE);
    }
}

```

- 불변 클래스에서는 정적 팩터리 메서드를 사용해 불필요한 객체 생성을 피할 수 있다.
- ```Boolean(String)```생성자 대신 ```Boolean.valueOf(String)```팩터리 메서드를 사용하는 것이 좋다.
    - 이 생성자는 자바 9 에서 deprecated API 로 지정
- 팩터리 메서드는 호출할 때마다 새로운 객체를 만들지 않는다(생성자와 다르게)
- 가변 객체라 해도 사용 중에 변경이 되지 않음을 안다면 재사용할 수 있다.
- 생성 비용이 아주 비싼 객체의 경우 캐싱하여 재사용하길 권한다.

```java
 // 코드 6-1 성능을 훨씬 더 끌어올릴 수 있다!
static boolean isRomanNumeralSlow(String s){
        return s.matches("^(?=.)M*(C[MD]|D?C{0,3})"
        +"(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
        }

```

- 내부에서 만드는 정규표현식 Pattern 인스턴스는, 한번 쓰고 버려져서 곧바로 가비지 컬렉션 대상이 된다.
- Pattern 은 입력받은 정규표현식에 해당하는 유한 상태 머신(finite state machine)을 만들기 때문에 인스턴스 생성 비용이 높다.
- 성능개선을 위해 (불변인) Pattern 인스턴스를 클래스 초기화(정적 초기화) 과정에서 직접 생성해 캐싱해두고, 나중에 isRomanNumeral 메서드가 호출될 떄마다 이 인스턴스를 재사용 한다.

```java
// 코드 6-2 값비싼 객체를 재사용해 성능을 개선한다.
private static final Pattern ROMAN=Pattern.compile(
        "^(?=.)M*(C[MD]|D?C{0,3})"
        +"(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

static boolean isRomanNumeralFast(String s){
        return ROMAN.matcher(s).matches();
        }
```

- 개선된 isRomanNumeral 방식의 클래스가 초기화된 후 이 메서드를 한번도 호출하지 않으면 ROMAL 필드는 쓸데없이 초기화된 꼴이다.
- isRomanNumeral 메서드가 처음 호출될 때 필드를 초기화하는 지연 초기화(lazy initialization)로 불필요한 초기화를 없앨 수도 있지만 권하지 않는다.
- 지연 초기화는 코드를 복잡하게 만들고 성능이 크게 개선되지 않을 때가 많기 때문이다.

### 아니 finite state machine 이 뭐냐고

어떠한 트랜지션(어떠한 조건 상태에서 변하는(이행하는 것)이 일어나는것을 뜻한다.<br>

FSM은 여러 분야에서 많이 쓰인다.. 그중 온라인 겜을 예를 들어 보자.<br>

온라인 게임에서 NPC라던가 몬스터가 FSM 한다.<br>
플레이어가 몇대 때리지도 않았는데 피하거나, 알아서 회복을 하거나, 심지어 패턴도 플레이어에 따라서 변한다.<br>
하지만 이 모든 것은 인공지능이 아닌 FSM에 기술된 약속에 의해 실행되는 약속된 패턴이다<br>

![image1](https://user-images.githubusercontent.com/71916223/160877939-ab96a759-902f-4cc3-96b0-79695a611d0e.jpg)

출처 : https://m.blog.naver.com/PostView.nhn?isHttpsRedirect=true&blogId=taehun3718&logNo=140162190370

## 2. 어댑터

### 어댑터(Adapter)패턴은 또 무엇인가..

한 클래스의 인터페이스를 사용하고자 하는 다른 인터페이스로 변환할 때 주로 사용하며, 이를 이용하면 인터페이스 호환성이 맞지 않아 같이 쓸 수 없는 클래스를 연관 관계로 연결해서 사용할 수 있게 해주는
패턴이다.<br>
**장점**

- 관계가 없는 인터페이스 간 같이 사용 가능
- 프로그램 검사 용이
- 클래스 재활용성 증가 등

**MediaPlayer.java**

```java
public interface MediaPlayer {

    void play(String filename);
}

```

**MediaPackage.java**

```java
public interface MediaPackage {

    void playFile(String filename);
}

```

**MP3.java**

```java
public class MP3 implements MediaPlayer {

    @Override
    public void play(String filename) {
        System.out.println("Playing MP3 File" + filename);
    }
}
```

**MP4.java**

```java
public class MP4 implements MediaPackage {

    @Override
    public void playFile(String filename) {
        System.out.println("Playing MP4 File " + filename);
    }
}

```

**MKV.java**

```java
public class MKV implements MediaPackage {

    @Override
    public void playFile(String filename) {
        System.out.println("Playing MKV File " + filename);
    }
}

```

**FormatAdapter.java**

```java
public class FormatAdapter implements MediaPlayer {

    private MediaPackage media;

    public FormatAdapter(MediaPackage media) {
        this.media = media;
    }

    @Override
    public void play(String filename) {
        System.out.println("Using Adapter --> ");
        media.playFile(filename);
    }
}
```

**Main.java**

```java
public class Main {

    public static void main(String[] args) {
        MediaPlayer player = new MP3();
        player.play("file.mp3");

        player = new FormatAdapter(new MP4());
        player.play("file.mp4");

        player = new FormatAdapter(new MKV());
        player.play("file.mkv");
    }
}

```

![image2](https://user-images.githubusercontent.com/71916223/160882694-5eddb000-c86d-4f56-8dcc-2701358e16b0.PNG)

- 객체는 불변이라면 재사용해도 안전함이 명백하다
- 하지만 덜 명확하고 심지어 직관에 반대되는 상황도 있다 = 어댑터
- 어탭터 : 실제 작업은 뒷단 객체에 위임하고, 자신은 제2의 인터페이스 역할을 해주는 객체이다.
- 어댑터는 뒷단 객체만 관리하면 된다.
- 자신은 제2의 인터페이스 역할을 해주는 객체다. 어댑터는 뒷단 객체만 관리하면 된다.
- 즉, 뒷단 객체 외에는 관리할 상태가 없으므로 뒷단 객체 하나당 어댑터 하나씩만 만들어지면 충분하다.
- 예컨대 Map 인터페이스의 keySet 메서드는 호출될 때마다 같은 Set 인스턴스를 반환한다.

```java
public interface Map<K, V> {
    Set<K> keySet();
}
```

_Returns a Collection view of the values contained in this map. The collection is backed by the map, so changes to the
map are reflected in the collection, and vice-versa. **If the map is modified while an iteration over the collection is
in progress (except through the iterator's own remove operation), the results of the iteration are undefined.** The
collection supports element removal, which removes the corresponding mapping from the map, via the Iterator.remove,
Collection.remove, removeAll, retainAll and clear operations. It does not support the add or addAll operations. Returns:
a collection view of the values contained in this map_

```java
public class HashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {
    // Map 객체 안의 키 전부를 담은 Set 뷰를 반환한다.
    public Set<K> keySet() {
        Set<K> ks = keySet; // 생성자로 새로 만드는 것이 아니다.
        if (ks == null) {
            ks = new KeySet();
            keySet = ks;
        }
        return ks;
    }

    
    final class KeySet extends AbstractSet<K> {
        public final int size() {
            return size;
        }

        public final void clear() {
            HashMap.this.clear();
        }

        public final Iterator<K> iterator() {
            return new KeyIterator();
        }

        public final boolean contains(Object o) {
            return containsKey(o);
        }

        public final boolean remove(Object key) {
            return removeNode(hash(key), key, null, false, true) != null;
        }

        public final Spliterator<K> spliterator() {
            return new KeySpliterator<>(HashMap.this, 0, -1, 0, 0);
        }

        public Object[] toArray() {
            return keysToArray(new Object[size]);
        }

        public <T> T[] toArray(T[] a) {
            return keysToArray(prepareArray(a));
        }

        public final void forEach(Consumer<? super K> action) {
            Node<K, V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (Node<K, V> e : tab) {
                    for (; e != null; e = e.next)
                        action.accept(e.key);
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
    }
}
```

```java
public class UsingKeySet {

    public static void main(String[] args) {
        Map<String, Integer> menu = new HashMap<>();
        menu.put("Burger", 8);
        menu.put("Pizza", 9);

        Set<String> names1 = menu.keySet();
        Set<String> names2 = menu.keySet();

        names1.remove("Burger");
        System.out.println(names2.size()); // 1
        System.out.println(menu.size()); // 1
    }
}
```

이 Set 인스턴스의 상태에 변경이 일어나면, 뒷단 객체에 해당하는 Map 인스턴스도 맞춰서 상태 변경이 일어난다.<br>
keySet이 Set 인터페이스를 매번 새로 만들어도 상관은 없지만, 그럴 필요도 없고 이득도 없다.<br>

그런데 백기선님은 견해가 달랐다. keySet 으로부터 받은 Set 인스턴스가 다른 곳에서도 사용중이고 심지어 상태값을 변화시킨다면 현재 내가 사용중인 Set 인스턴스와 Map 인스턴스의 값에 확신을 할 수 없다.
그렇기 때문에 매번 복사해서 새로운 객체를 반환하도록 하는(item 50) 방어적 복사 방식을 선호한다고 했다. 나도 백기선님의 견해에 동의한다. 상황에 따라 다르겠지만, 일반적으로 Set 인터페이스가 매번
생성된다고 해도 성능에 치명적이지 않을 것 같다. 오히려 Map 인스턴스와 Set 인스턴스를 Immutable하게 사용하는 것이 유지보수 하는데 더 많은 이점을 가져다 줄 것 같다.

## 3. 오토 박싱(auto boxing)

기본 타입과 박싱된 기본 타입을 섞어 쓸 때 자동으로 상호 변환해주는 기술<br>
오토박싱은 기본 타입과 그에 대응하는 박싱된 기본 타입의 구분을 흐려주지만, 완전히 없애주는 것은 아니다.<br>
의미상으로는 별다를 것 없지만 성능에서는 그렇지 않다 (item 61)
> 오토박싱(auto boxing) : 컴파일러에 의해 기본타입이 래퍼 클래스로 자동 변환<br>
> 오토언박싱(auto unboxing) : 컴파일러에 의해 래퍼 클래스가 기본타입으로 자동 변환

```java
// 오토 박싱
int i=10;
        Integer num=i;

// 오토 언박싱
        Integer num=new Integer(10);
        int i=num;
```

```java
private static long sum(){
        Long sum=0L;
        for(long i=0;i<=Integer.MAX_VALUE;i++)
        sum+=i;
        return sum;
        }
```

- 하지만 Long으로 선언된 변수를 long으로 바꾸면 훨씬 더 빠른 프로그램이 된다.
- 위 코드는 sum 변수를 Long으로 선언했기 때문에 불필요한 Long 인스턴스가 약 2^31개나 만들어진다. (long 타입인 i가 Long 타입인 sum에 더해질 때마다 생성된다.)
- 박싱된 기본 타입보다는 기본 타입을 사용하고, 의도치 않은 오토박싱이 숨어들지 않도록 주의하자.

## 4. 오해 금지

- 이번 아이템을 "객체 생성은 비싸니 피해야 한다"로 오해하면 안 된다.
- 특히나 요즘의 JVM에서는 별다른 일을 하지 않는 작은 객체를 생성하고 회수하는 일이 크게 부담되지 않는다.
- 프로그램의 명확성, 간결성, 기능을 위해서 객체를 추가로 생성하는 것이라면 일반적으로 좋은 일이다.

**그렇다고 자신만의 객체 풀(pool)을 만들지는 말자**<br>

- 아주 무거운 객체가 아닌 이상, 단순히 객체 생성을 피하기 위해 자신만의 객체 풀(pool)을 만들지는 말자.
- DB 커넥션 같은 경우 생성 비용이 워낙 비싸니 재사용하는 편이 낫다.
- 하지만 일반적으로는 자체 객체 풀은 코드를 헷갈리게 만들고 메모리 사용량을 늘리고 성능을 떨어뜨린다.
- 요즘 JVM의 GC는 상당히 잘 최적화되어서, 가벼운 객체를 다룰 때는 직접 만든 객체 풀보다 훨씬 빠르다.

## 5. 재사용하자 vs 재사용하지 말자

- 이번 아이템은 객체를 재사용할 것을 지향한다.
- 하지만 item 50은 방어적 복사를 통해 재사용을 지양한다.
- 두개를 비교해본다면, 객체를 재사용했을때의 피해가 더 크다.
- 방어적 복사에 실패했을 댸 동기화 관련한 이슈가 발생할 수 있고 보안에 구멍이 뚫릴수도 있기 때문이다.
- 불필요한 객체 생성은 그저 코드 형태와 성능에만 영향을 끼친다.
- 가장 최선은 객체를 재사용할지, 방어적 복사를 통해 재사용을 안할지 상황에 맞춰 '잘' 판단하는 것이다.