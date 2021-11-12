package it.uniba.map.basketmarketanlysis.model.mining;

import it.uniba.map.basketmarketanlysis.model.data.ContinuousAttribute;

public class ContinuousItem extends Item {

    public ContinuousItem(ContinuousAttribute attribute, Interval value) {
        super(attribute, value);
    }

    /**
     * @param value un numero reale
     * @return true se value è compreso nell'intervallo dell'item,
     * false altrimenti
     */
    @Override
    boolean checkItemCondition(Object value) {

        if (value instanceof Float && this.value instanceof Interval) {
            Interval interval = (Interval) this.value;
            return interval.checkValueInclusion((Float) value);
        }
        throw new ClassCastException();

    }

    @Override
    public String toString() {
        return attribute + " in " + value;
    }
}
