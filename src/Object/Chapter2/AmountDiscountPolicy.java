package Object.Chapter2;

public class AmountDiscountPolicy extends DiscountPolicy{
    private Money discountAmount;

    // 할인 금액을 추가로 생성자로 받는다
    public AmountDiscountPolicy(Money discountAmount, DiscountCondition ... conditions){
        super(conditions);
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money getDiscountAmount(Screening screening){
        return discountAmount;
    }
}
