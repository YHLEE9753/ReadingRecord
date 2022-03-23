package Object.Chapter5;

import java.time.Duration;

public class Movie {
    // 5. 인스턴스 변수설정
    private String title;
    private Duration runningTime;
    private Money fee;
    private List<DiscountCondition> discountConditions;

    private MovieType movieType;
    private Money discountAmount;
    private double discountPercent;

    // 4. 영화비 계산을 위해 메서드 구현(책임 결정)
    public Money calculateMovieFee(Screening screening){
        if(isDiscountable(screening)){
            return fee.minus(calculateMovieFee());
        }
        return fee;
    }

    private boolean isDiscountable(Screening screening){
        return discountConditions.stream().
                anyMatch(condition -> condition.isSatisfiedBy(screening));
    }

    private Money calculateDiscountAmount(){
        switch (movieType){
            case AMOUNT_DISCOUNT:
                return calculateAmountDiscountAmount();
            case PERCENT_DISCOUNT:
                return calculatePercentDiscountAmount();
            case NONE_DISCOUNT:
                return calculateNoneDiscountAmount();
        }
        throw new IllegalStateException();
    }

    private Money calculateAmountDiscountAmount(){
        return discountAmount;
    }

    private Money calculatePercentDiscountAmount(){
        return fee.times(discountPercent);
    }

    private Money calculateNoneDiscountAmount(){
        return Money.ZERO
    }
}
