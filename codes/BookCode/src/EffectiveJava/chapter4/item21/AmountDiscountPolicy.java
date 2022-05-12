package EffectiveJava.chapter4.item21;

import java.util.List;

public class AmountDiscountPolicy implements DiscountPolicy{
    @Override
    public List<String> getConditions() {
        return null;
    }

    @Override
    public int getDiscountAmount(Object movie) {
        return 0;
    }

    @Override
    public String publicInterface() {
        return null;
    }
}
