package it.uniba.map.marketbasketanlysis.model.mining;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import it.uniba.map.marketbasketanlysis.model.Data;
import it.uniba.map.marketbasketanlysis.model.data.Attribute;


public class FrequentPattern implements Comparable<FrequentPattern>, Iterable<Item>, Serializable {

    private final LinkedList<Item> fp;
    private float support;


    public FrequentPattern() {
        fp = new LinkedList<>();
    }

    /**
     *  Costruttore per copia: crea un nuovo frequent pattern a partire da un altro.
     * @param pattern frequent pattern to copy
     */
    public FrequentPattern(FrequentPattern pattern) {
        fp = new LinkedList<>();
        fp.addAll(pattern.fp);
        support = pattern.support;
    }

    /**
     * Aggiunge un nuovo item al pattern
     * @param item item da aggingere al frequent pattern
     */
    public void addItem(Item item) {
        fp.add(item);
    }

    /**
     *
     * @param index
     * @return
     */
    public Item getItem(int index) {
        return fp.get(index);
    }

    /**
     * Restituisce il support del fp
     * @return support
     */
    public float getSupport() {
        return support;
    }

    /**
     * Assegna una support rate al frequent pattern
     * @param support
     */
    public void setSupport(float support) {
        this.support = support;
    }

    /**
     * @return Frequent Pattern number of items
     */
    public int getPatternLength() {
        return fp.size();
    }

    @NonNull
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Item last = fp.removeLast();
        fp.forEach(i -> builder.append(String.format("(%s) AND ", i)));
        fp.add(last);
        builder.append(String.format("(%s) [%s]", last, support));
        return builder.toString();
    }

    /**
     * Calcola il support del frequent pattern
     * @param data Data table
     * @return support
     */
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
