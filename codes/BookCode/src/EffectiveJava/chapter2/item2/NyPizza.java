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
