package it.uniba.map.basketmarketanlysis.model.data;

import java.util.Iterator;


/**
 * Iteratore che implementa l'algoritmo di discretizzazione
 * di un intervallo
 */
public class ContinuousAttributeIterator implements Iterator<Float> {

    private final float min;
    private final float max;
    private final int numValues;
    private int j = 0;

    public ContinuousAttributeIterator(float min, float max, int numValues) {
        this.min = min;
        this.max = max;
        this.numValues = numValues;
    }

    @Override
    public boolean hasNext() {
        return j <= numValues;
    }

    public Float next() {
        j++;
        if (j - 1 == numValues) return max + 0.0606f;
        return min + ((max - min) / numValues) * (j - 1);
    }

    public void remove() {


    }

}
