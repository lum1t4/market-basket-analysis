package it.uniba.map.basketmarketanlysis.model.mining;

import java.io.Serializable;

public class Interval implements Serializable {
    private float inf;
    private float sup;

    public Interval(float inf, float sup) {
        this.inf = inf;
        this.sup = sup;
    }

    /**
     * Restiuisce l'estremo inferiore dell'intervallo
     * @return inf estremo inferiore dell'intervallo
     */
    public float getInf() {
        return inf;
    }

    /**
     * Imposta l'estremo inferiore dell'intervallo
     * @param inf inferiore dell'intervallo
     */
    public void setInf(float inf) {
        this.inf = inf;
    }

    /**
     * Restiuisce l'estremo superiore dell'intervallo
     * @return sup estremo superiore dell'intervallo
     */
    public float getSup() {
        return sup;
    }

    /**
     * Imposta l'estremo superiore dell'intervallo
     * @param sup superiore dell'intervallo
     */
    public void setSup(float sup) {
        this.sup = sup;
    }

    /**
     * Contralla se il valore è incluso nell'intervallo
     * @param value float
     * @return true se value è compreso [a, b[ , false altrimenti
     */
    boolean checkValueInclusion(float value) {
        return inf <= value && value < sup;
    }


    /**
     * Rappresentazione testuale di <code>{@code Interval}</code>
     * @return string
     */
    public String toString() {
        return "[" + inf + ", " + sup + "[";
    }

}
