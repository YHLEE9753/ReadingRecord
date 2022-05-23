package EffectiveJava.chapter5.item26;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        List<?> strings = new ArrayList<>();
        strings.add(null);
        strings.add("sout");
    }
}
