package it.uniba.map.basketmarketanlysis.model.mining;

import java.util.Comparator;

/**
 * Classe funzionale (un solo metodo) che compare due emergin pattern
 * in base al grow rate
 */
public class ComparatorGrowRate implements Comparator<EmergingPattern> {

    @Override
    public int compare(EmergingPattern o1, EmergingPattern o2) {
        return Float.compare(o1.getGrowRate(), o2.getGrowRate());
    }

}
