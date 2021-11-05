package it.uniba.map.basketmarketanlysis.model.data;

import java.util.Iterator;

public class ContinuousAttribute extends Attribute implements Iterable<Float> {

    private final float max;
    private final float min;

    public ContinuousAttribute(String name, int index, float min, float max) {
        super(name, index);
        this.min = min;
        this.max = max;
    }

    public float getMax() {
        return max;
    }

    public float getMin() {
        return min;
    }

    @Override
    public Iterator<Float> iterator() {
        return new ContinuousAttributeIterator(min, max, 5);
    }
}
