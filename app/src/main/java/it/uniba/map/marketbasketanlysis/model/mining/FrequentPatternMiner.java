package it.uniba.map.marketbasketanlysis.model.mining;

import androidx.annotation.NonNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import it.uniba.map.marketbasketanlysis.model.Data;
import it.uniba.map.marketbasketanlysis.model.data.Attribute;
import it.uniba.map.marketbasketanlysis.model.data.ContinuousAttribute;
import it.uniba.map.marketbasketanlysis.model.data.DiscreteAttribute;
import it.uniba.map.marketbasketanlysis.utility.EmptySetException;
import it.uniba.map.marketbasketanlysis.utility.Queue;

public class FrequentPatternMiner implements Iterable<FrequentPattern>, Serializable {

    private final List<FrequentPattern> outputFP = new LinkedList<>();

    public FrequentPatternMiner(Data data, float minSup) throws EmptySetException {

        if (data.getNumberOfExamples() <= 0) {
            throw new EmptySetException();
        }

        Queue<FrequentPattern> fpQueue = new Queue<>();

        for (int i = 0; i < data.getNumberOfAttributes(); i++) {
            Attribute attribute = data.getAttribute(i);
            if (attribute instanceof DiscreteAttribute) {
                DiscreteAttribute discreteAttribute = (DiscreteAttribute) attribute;
                for (int j = 0; j < ((DiscreteAttribute) attribute).getNumberOfDistinctValues(); j++) {
                    DiscreteItem item = new DiscreteItem(discreteAttribute, discreteAttribute.getValue(j));
                    FrequentPattern fp = new FrequentPattern();
                    fp.addItem(item);
                    fp.setSupport(fp.computeSupport(data));
                    // 1-FP CANDIDATE
                    if (fp.getSupport() >= minSup) {
                        fpQueue.enqueue(fp);
                        outputFP.add(fp);
                    }
                }
            } else if (attribute instanceof ContinuousAttribute) {
                ContinuousAttribute continuousAttribute = (ContinuousAttribute) attribute;
                Iterator<Float> it = continuousAttribute.iterator();
                Float min = it.next();
                while (it.hasNext()) {
                    float next = it.next();
                    ContinuousItem item = new ContinuousItem(continuousAttribute, new Interval(min, next));
                    FrequentPattern fp = new FrequentPattern();
                    fp.addItem(item);
                    fp.setSupport(fp.computeSupport(data));
                    if (fp.getSupport() >= minSup) {
                        fpQueue.enqueue(fp);
                        outputFP.add(fp);
                    }
                    min = next;
                }

            }

        }

        expandFrequentPatterns(data, minSup, fpQueue, outputFP);
        sort();
    }

    public static FrequentPatternMiner carica(String nomeFile) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(nomeFile);
        ObjectInputStream inputStream = new ObjectInputStream(file);
        return (FrequentPatternMiner) inputStream.readObject();
    }

    private void expandFrequentPatterns(Data data, float minSup, Queue<FrequentPattern> queue, List<FrequentPattern> outputFP) {

        while (!queue.isEmpty()) {

            FrequentPattern frequentPattern = queue.first(); // frequentPattern to be refined
            queue.dequeue();

            for (int i = 0; i < data.getNumberOfAttributes(); i++) {
                Attribute attribute = data.getAttribute(i);

                boolean found = false;
                //the new item should involve an attribute different form attributes already involved into the items of fp
                for (Item item : frequentPattern) {
                    if (item.getAttribute().equals(attribute)) {
                        found = true;
                        break;
                    }
                }

                // data.getAttribute(i) is not involve into an item of fp
                if (!found) {
                    if (attribute instanceof DiscreteAttribute) {
                        DiscreteAttribute discreteAttribute = (DiscreteAttribute) attribute;
                        for (int j = 0; j < discreteAttribute.getNumberOfDistinctValues(); j++) {
                            DiscreteItem item = new DiscreteItem(discreteAttribute, discreteAttribute.getValue(j));
                            FrequentPattern newFP = refineFrequentPattern(frequentPattern, item); //generate refinement
                            newFP.setSupport(newFP.computeSupport(data));

                            if (newFP.getSupport() >= minSup) {
                                queue.enqueue(newFP);
                                outputFP.add(newFP);
                            }
                        }
                    } else if (attribute instanceof ContinuousAttribute) {
                        ContinuousAttribute continuousAttribute = (ContinuousAttribute) attribute;
                        Iterator<Float> it = continuousAttribute.iterator();
                        Float min = it.next();

                        while (it.hasNext()) {
                            Float next = it.next();
                            ContinuousItem item = new ContinuousItem(continuousAttribute, new Interval(min, next));
                            FrequentPattern newFP = refineFrequentPattern(frequentPattern, item); //generate refinement
                            newFP.setSupport(newFP.computeSupport(data));

                            if (newFP.getSupport() >= minSup) {
                                queue.enqueue(newFP);
                                outputFP.add(newFP);
                            }
                            min = next;
                        }
                    }

                }

            }
        }
    }

    private FrequentPattern refineFrequentPattern(FrequentPattern fp, Item item) {
        FrequentPattern newFp = new FrequentPattern(fp);
        newFp.addItem(item);
        return newFp;
    }

    @NonNull
    public String toString() {
        StringBuilder builder = new StringBuilder();
        outputFP.forEach(fp -> builder.append(fp).append(System.lineSeparator()));
        return builder.toString();
    }

    private void sort() {
        //outputFP.sort((a,b)->Float.compare(a.getSupport(),b.getSupport()));
        Collections.sort(outputFP);
    }

    @NonNull
    @Override
    public Iterator<FrequentPattern> iterator() {
        return outputFP.iterator();
    }

    public void salva(String nomeFile) throws IOException {
        FileOutputStream file = new FileOutputStream(nomeFile);
        ObjectOutputStream outputStream = new ObjectOutputStream(file);
        outputStream.writeObject(this);

    }

}
	


