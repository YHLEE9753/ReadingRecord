## 디자인 패턴의 종류
### COMPOSITE 패턴
COMPOSITE 패턴 : 클라이언트가 개별 객체와 복합 객체를 동일하게 취급하는 패턴, 여기서 컴포지트의 의도는 트리 구조로 작성하여, 전체-부분(whole-part) 관계를 표현하는 것이다.<br>
언제사용하는가?<br>
- 전체-부분 관계를 트리 구조로 표현하고 싶을 경우.
- 전체-부분 관계를 클라이언트에서 부분, 관계 객체를 균일하게 처리하고 싶을 경우.

예) 여러개의 discount 정책을 List 로 담고 싶을때<br>

![그림1](https://user-images.githubusercontent.com/71916223/154431461-39b16113-17cc-472d-b2a4-2e8d618e8736.PNG)

<br>
<br>
COMPOSITE 패턴 구조<br>

<img width="450" alt="그림2" src="https://user-images.githubusercontent.com/71916223/154431474-ef6662e9-166d-4147-a36b-0276f2dbb017.png">

<br>
"Client" 클래스는 "Leaf" 와 "Composite" 클래스를 직접 참조하지 않고, 공통 인터페이스 "Component" 를 참조하는 것을 볼 수 있다.<br>
"Leaf" 클래스는 "Component" 인터페이스를 구현한다.<br>
"Composite" 클래스는 "Component" 객체 자식들을 유지하고, operation() 과 같은 요청을 통해 자식들에게 전달한다.<br>
<br>
각각을 조금 더 코드 관점에서 보면 다음과 같다.<br>
Component : 모든 component 들을 위한 추상화된 개념으로써, "Leaf" 와 "Composite" 클래스의 인터페이스이다.<br>
Leaf: "Component" 인터페이스를 구현하고, 구체 클래스를 나타낸다.<br>
Composite : "Component"  인터페이스를 구현하고, 구현되는 자식(Leaf or Composite) 들을 가지고, 이러한 자식들을 관리하기 위한 메소드(addChild, removeChild...)를 구현한다. 또한, 일반적으로 인터페이스에 작성된 메소드는 자식에게 위임하는 처리를 한다.<br>

*Composite.operation() => Leaf.operation(), 자세한 이해는 아래 예제를 통해 할 수 있다.*<br>
<br>

<img width="450" alt="그림3" src="https://user-images.githubusercontent.com/71916223/154431469-b8bf6df6-a373-41f5-9b5d-4a5ad2b58d2c.png">

<br>

```java
/** "Component" */
interface Graphic {
    //Prints the graphic.
    public void print();
}
```
```java
/** "Leaf" */
class Ellipse implements Graphic {

    //Prints the graphic.
    public void print() {
        System.out.println("Ellipse");
    }
}
```
```java
/** "Composite" */
class CompositeGraphic implements Graphic {

    //Collection of child graphics.
    private List<Graphic> childGraphics = new ArrayList<Graphic>();

    //Prints the graphic.
    public void print() {
        for (Graphic graphic : childGraphics) {
            graphic.print();  //Delegation
        }
    }

    //Adds the graphic to the composition.
    public void add(Graphic graphic) {
        childGraphics.add(graphic);
    }

    //Removes the graphic from the composition.
    public void remove(Graphic graphic) {
        childGraphics.remove(graphic);
    }
}
```
```java
/** Client */
public class Program {
    public static void main(String[] args) {
        //Initialize four ellipses 
        Ellipse ellipse1 = new Ellipse();
        Ellipse ellipse2 = new Ellipse();
        Ellipse ellipse3 = new Ellipse();
        Ellipse ellipse4 = new Ellipse();
        //Initialize three composite graphics 
        CompositeGraphic graphic = new CompositeGraphic();
        CompositeGraphic graphic1 = new CompositeGraphic();
        CompositeGraphic graphic2 = new CompositeGraphic();

        //Composes the graphics 
        graphic1.add(ellipse1); // children - leaf 
        graphic1.add(ellipse2); // children - leaf 
        graphic1.add(ellipse3); // children - leaf 

        graphic2.add(ellipse4); // children - leaf 

        graphic.add(graphic1); // children - composite
        graphic.add(graphic2); // children - composite 

        // Prints the complete graphic (Four times the string "Ellipse"). 
        graphic.print();
    }
}

```

<img width="450" alt="그림4" src="https://user-images.githubusercontent.com/71916223/154431470-b2502f83-3045-4357-ab1a-b9805d73170a.png">

<br>
지금까지 다룬 방식은 타입의 안정성을 추구하는 방식이다.

이것은 자식을 다루는 add(), remove() 와 같은 메소드들은 오직 "Composite" 만 정의되었다.

그로 인해, "Client" 는 "Leaf" 와 "Composite" 를 다르게 취급하고 있다.

하지만 "Client" 에서 "Leaf" 객체가 자식을 다루는 메소드를 호출할 수 없기 때문에, 타입에 대한 안정성을 얻게 된다.


```java
Ellipse ellipse = new Ellipse();

CompositeGraphic graphic = new CompositeGraphic();

```



다른 방식으로 일관성을 추구하는 방식은, 자식을 다루는 메소드들을 "Composite" 가 아닌 "Component" 에 정의한다.

그로 인해, "Client" 는 "Leaf" 와 "Composite" 를 일관되게 취급할 수 있다.

하지만 "Client" 는 "Leaf" 객체가 자식을 다루는 메소드를 호출할 수 있기 때문에, 타입의 안정성을 잃게 된다.


```java
Graphic ellipse = new Ellipse();

Graphic graphic = new CompositeGraphic();
```




어떤 방식이 더 좋냐를 따지기에는 너무 많은 것이 고려된다.

위키에서의 이론은 컴포지트 패턴은 타입의 안정성보다는 일관성을 더 강조한다고 한다.



출처: https://mygumi.tistory.com/343 [마이구미의 HelloWorld], 오브젝트(도서)
