package it.uniba.map.basketmarketanlysis.model.mining;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import it.uniba.map.basketmarketanlysis.model.Data;
import it.uniba.map.basketmarketanlysis.model.data.Attribute;


public class FrequentPattern implements Comparable<FrequentPattern>, Iterable<Item>, Serializable {

    private final LinkedList<Item> fp;
    private float support;


    FrequentPattern() {
        fp = new LinkedList<>();
    }

    // costruttore per copia
    FrequentPattern(FrequentPattern pattern) {
        fp = new LinkedList<>();
        fp.addAll(pattern.fp);
        support = pattern.support;
    }

    // aggiunge un nuovo item al pattern
    void addItem(Item item) {
        fp.add(item);
    }

    Item getItem(int index) {
        return fp.get(index);
    }

    /**
     * @return support
     */
    public float getSupport() {
        return support;
    }

    public void setSupport(float support) {
        this.support = support;
    }

    public int getPatternLength() {
        return fp.size();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        Item last = fp.removeLast();
        fp.forEach(i -> builder.append(String.format("(%s) AND ", i)));
        fp.add(last);
        builder.append(String.format("(%s) [%s]", last, support));

        return builder.toString();
    }

    // Aggiorna il supporto
    public float computeSupport(Data data) {
        int suppCount = 0;
        // indice esempio
        for (int i = 0; i < data.getNumberOfExamples(); i++) {
            //indice item
            boolean isSupporting = true;

            for (Item item : this) {
                Attribute attribute = item.getAttribute();
                Object valueInExample = data.getAttributeValue(i, attribute.getIndex());
                if (!item.checkItemCondition(valueInExample)) {
                    isSupporting = false;
                    break; //the ith example does not satisfy fp
                }
            }
            if (isSupporting) {
                suppCount++;
            }

        }
        return ((float) suppCount) / (data.getNumberOfExamples());
    }


    @Override
    public int compareTo(FrequentPattern frequentPattern) {
        return Float.compare(support, frequentPattern.getSupport());
    }

    @Override
    public Iterator<Item> iterator() {
        return fp.iterator();
    }


}
