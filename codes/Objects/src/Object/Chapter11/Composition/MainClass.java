package Object.Chapter11.Composition;

import java.time.Duration;

public class MainClass {
    public static void main(String[] args) {
        Phone phone1 = new Phone(new RegularPolicy(Money.wons(10), Duration.ofSeconds(10)));
        Phone phone2 = new Phone(new TaxablePolicy(new RegularPolicy(Money.wons(10), Duration.ofSeconds(10)), 0.05));
    }
}
