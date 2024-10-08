package it.uniba.map.marketbasketanlysis.model.data;

import java.util.Iterator;



public class ContinuousAttribute extends Attribute implements Iterable<Float> {

    private final float max;
    private final float min;

    /**
     * Costruttore di Continuous Attribute
     * @param name string nomee dell'attributo
     * @param index indice dell'attributo
     * @param min minimo tra i valori dell'attributo
     * @param max massimo tra i valori dell'attributo
     */
    public ContinuousAttribute(String name, int index, float min, float max) {
        super(name, index);
        this.min = min;
        this.max = max;
    }

    /**
     * @return massimo tra i valori dell'attributo
     */
    public float getMax() {
        return max;
    }

    /**
     * @returns minimo tra i valori dell'attributo
     */
    public float getMin() {
        return min;
    }

    /**
     * @return un iteratore che discretizza l'intervallo tra minimo e massimo (escluso)
     */
    @Override
    public Iterator<Float> iterator() {
        return new ContinuousAttributeIterator(min, max, 5);
    }
}
