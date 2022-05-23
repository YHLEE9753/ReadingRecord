package EffectiveJava.chapter5.item26;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class RawType {
    private final static Collection<Stamp> stamps = new ArrayList();

    public static void main(String[] args) {

        stamps.add(new Stamp("우편"));
//        stamps.add(new Coin("500"));


        for (Iterator i = stamps.iterator(); i.hasNext(); ) {
            Stamp stamp = (Stamp) i.next();
            stamp.check();
        }
    }
}
