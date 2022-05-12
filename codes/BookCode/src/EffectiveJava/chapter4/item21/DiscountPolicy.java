package EffectiveJava.chapter4.item21;

import java.util.List;

public interface DiscountPolicy {
    default int calculateDiscountAmount(Object movie){
        for(String each : getConditions()){
            if(each.equals((String) movie)){
                return getDiscountAmount(movie);
            }
        }
        return 0;
    }

    List<String> getConditions();
    int getDiscountAmount(Object movie);
    String publicInterface();
}
