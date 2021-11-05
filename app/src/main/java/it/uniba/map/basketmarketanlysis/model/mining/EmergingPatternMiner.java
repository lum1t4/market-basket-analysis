package it.uniba.map.basketmarketanlysis.model.mining;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import it.uniba.map.basketmarketanlysis.model.Data;
import it.uniba.map.basketmarketanlysis.utility.EmptySetException;

public class EmergingPatternMiner implements Iterable<EmergingPattern>, Serializable {

    List<EmergingPattern> epList = new LinkedList<>();

    public EmergingPatternMiner(Data dataBackground, FrequentPatternMiner fpList, float minG) throws EmptySetException {

        if (dataBackground.getNumberOfExamples() <= 0) {
            throw new EmptySetException();
        }

        for (FrequentPattern fp : fpList) {
            try {
                EmergingPattern ep = computeEmergingPattern(dataBackground, fp, minG);
                epList.add(ep);
            } catch (EmergingPatternException ignored) {
            }
        }

        sort();
    }



    public float computeGrowRate(Data dataBackground, FrequentPattern fp) {
        return fp.getSupport() / fp.computeSupport(dataBackground);
    }

    public EmergingPattern computeEmergingPattern(Data dataBackground, FrequentPattern fp, float minGR) throws EmergingPatternException, EmptySetException {
        float growRate = computeGrowRate(dataBackground, fp);
        if (growRate > minGR) {
            return new EmergingPattern(fp, growRate);
        }
        throw new EmergingPatternException();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        epList.forEach(ep -> builder.append(ep).append(System.lineSeparator()));
        return builder.toString();
    }

    @Override
    public Iterator<EmergingPattern> iterator() {
        return epList.iterator();
    }

    private void sort() {
        epList.sort(new ComparatorGrowRate());
    }

    public void salva(String nomeFile) throws IOException {
        FileOutputStream file = new FileOutputStream(nomeFile);
        ObjectOutputStream outputStream = new ObjectOutputStream(file);
        outputStream.writeObject(this);
    }

}
