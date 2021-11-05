package it.uniba.map.basketmarketanlysis.model.mining;

import java.io.Serializable;

public class Interval implements Serializable {
    private float inf;
    private float sup;

    public Interval(float inf, float sup) {
        this.inf = inf;
        this.sup = sup;
    }

    public float getInf() {
        return inf;
    }

    public void setInf(float inf) {
        this.inf = inf;
    }

    public float getSup() {
        return sup;
    }

    public void setSup(float sup) {
        this.sup = sup;
    }

    boolean checkValueInclusion(float value) {
        return inf <= value && value < sup;
    }

    public String toString() {
        return "[" + inf + ", " + sup + "[";
    }

}
