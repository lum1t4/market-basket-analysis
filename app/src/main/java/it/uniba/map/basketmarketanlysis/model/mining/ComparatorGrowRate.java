package it.uniba.map.basketmarketanlysis.model.mining;

import java.util.Comparator;

public class ComparatorGrowRate implements Comparator<EmergingPattern> {

    @Override
    public int compare(EmergingPattern o1, EmergingPattern o2) {
        return Float.compare(o1.getGrowRate(), o2.getGrowRate());
    }

//    @Override
//    public Comparator<EmergingPattern> thenComparing(Comparator<? super EmergingPattern> other) {
//        return Comparator.super.thenComparing(other);
//    }
}
