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
