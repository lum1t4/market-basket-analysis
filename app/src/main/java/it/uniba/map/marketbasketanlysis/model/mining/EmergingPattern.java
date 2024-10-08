package it.uniba.map.marketbasketanlysis.model.mining;


import java.io.Serializable;

import it.uniba.map.marketbasketanlysis.utility.EmptySetException;

public class EmergingPattern extends FrequentPattern implements Serializable {
    private final float growRate;

    public EmergingPattern(FrequentPattern fp, float growRate) throws EmptySetException {
        super(fp);
        this.growRate = growRate;
    }

    /**
     * @return tasso di crescita
     */
    float getGrowRate() {
        return growRate;
    }

    /**
     * Codifica in stringa del pattern emergente
     */
    @Override
    public String toString() {
        return super.toString() + "[" + growRate + "]";
    }

}
