package it.uniba.map.marketbasketanlysis.model.data;

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

    /**
     *
     * @param min estremo inferiore
     * @param max estremo superiore
     * @param numValues numero di campionamenti da effettuare
     */
    public ContinuousAttributeIterator(float min, float max, int numValues) {
        this.min = min;
        this.max = max;
        this.numValues = numValues;
    }


    /**
     * @return true se è possibile scandire l'intervallo
     */
    @Override
    public boolean hasNext() {
        return j <= numValues;
    }

    /**
     * @return il prossimo valore
     */
    public Float next() {
        j++;
        if (j - 1 == numValues) return max + 0.0606f;
        return min + ((max - min) / numValues) * (j - 1);
    }

    public void remove() {


    }

}
