package it.uniba.map.basketmarketanlysis.model.mining;


import java.io.Serializable;

import it.uniba.map.basketmarketanlysis.utility.EmptySetException;

public class EmergingPattern extends FrequentPattern implements Serializable {
    private final float growRate;

    public EmergingPattern(FrequentPattern fp, float growRate) throws EmptySetException {
        super(fp);
        this.growRate = growRate;
    }

    float getGrowRate() {
        return growRate;
    }

    @Override
    public String toString() {
        return super.toString() + "[" + growRate + "]";
    }


}
