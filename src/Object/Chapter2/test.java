package Object.Chapter2;

import java.time.Duration;
//4
public class test {
    public static void main(String[] args) {
        Movie avatar1 = new Movie("아바타",
                Duration.ofMinutes(120),
                Money.wons(10000),
                new AmountDiscountPolicy(Money.wons(800)));

        Movie avatar2 = new Movie("아바타",
                Duration.ofMinutes(120),
                Money.wons(10000),
                new PercentDiscountPolicy(0.1));

        Movie starwars = new Movie("스타워즈",
                Duration.ofMinutes(210),
                Money.wons(10000),
                new NoneDiscountPolicy());
    }
}
