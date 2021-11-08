package it.uniba.map.basketmarketanlysis.model;


import androidx.annotation.NonNull;

import org.w3c.dom.Attr;

import java.util.List;

import it.uniba.map.basketmarketanlysis.model.data.Attribute;
import it.uniba.map.basketmarketanlysis.model.data.Basket;
import it.uniba.map.basketmarketanlysis.model.data.Dataset;

public class Data {

    private final Object[][] data;
    private final int numberOfExamples;
    private final List<Attribute> attributeSet;


    public Data(Dataset dataset) {
        List<Basket> baskets = dataset.getBaskets();
        numberOfExamples = baskets.size();
        attributeSet = dataset.getAttributes();
        data = new Object[numberOfExamples][attributeSet.size()];
        for (int i = 0; i < baskets.size(); i++) {
            for (Attribute attribute : attributeSet) {
                data[i][attribute.getIndex()] = baskets.get(i).getValue(attribute);
            }
        }
    }


    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    public int getNumberOfAttributes() {
        return attributeSet.size();
    }


    public Object getAttributeValue(int exampleIndex, int attributeIndex) {
        return data[exampleIndex][attributeSet.get(attributeIndex).getIndex()];
    }

    public Attribute getAttribute(int index) {
        return attributeSet.get(index);
    }

    @NonNull
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numberOfExamples; i++) {
            builder.append(i + 1)
                    .append(":");
            for (int j = 0; j < attributeSet.size() - 1; j++) {
                builder.append(data[i][j]).append(",");
            }
            builder.append(data[i][attributeSet.size() - 1])
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }


}