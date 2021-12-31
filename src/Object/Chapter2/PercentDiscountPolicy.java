package Object.Chapter2;

public class PercentDiscountPolicy extends DiscountPolicy{
    private double percent;

    // 퍼센트를 추가로 생성자로 받는다
    public PercentDiscountPolicy(double percent, DiscountCondition ... conditions){
        super(conditions);
        this.percent = percent;
    }

    @Override
    protected Money getDiscountAmount(Screening screening){
        return screening.getMovieFee().times(percent);
    }
}
