package Object.Chapter11.Composition;


import java.math.BigDecimal;

//2
public class Money {
    public static final Money ZERO = Money.wons(0);

    private final BigDecimal amount;
    // BigDecimal : 숫자를 정밀하게 저장하고 표현할 수 있는 유일한 방법 - 돈과 소수점을 다룰 떄는 필수


    public static Money wons(long amount){
        return new Money(BigDecimal.valueOf(amount));
        // 문자열로 return
    }

    public static Money wons(double amount){
        return new Money(BigDecimal.valueOf(amount));
        //문자열로 return
    }

    Money(BigDecimal amount){
        this.amount = amount;
        // 생성자
    }

    public Money plus(Money amount){
        return new Money(this.amount.add(amount.amount));
        //문자열로 계산
    }

    public Money minus(Money amount){
        return new Money(this.amount.subtract(amount.amount));
    }

    public Money times(double percent){
        return new Money(this.amount.multiply(
                BigDecimal.valueOf(percent)));
    }

    public boolean isLessThan(Money other){
        return amount.compareTo(other.amount) < 0;
    }

    public boolean isGreaterThanOrEqual(Money other){
        return amount.compareTo(other.amount) >= 0;
    }
}