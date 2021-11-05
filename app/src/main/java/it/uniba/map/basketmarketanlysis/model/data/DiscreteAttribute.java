package it.uniba.map.basketmarketanlysis.model.data;

public class DiscreteAttribute extends Attribute {
    private final String[] values;

    public DiscreteAttribute(String name, int index, String[] values) {
        super(name, index);
        this.values = values;
    }

    public int getNumberOfDistinctValues() {
        return values.length;
    }

    public String getValue(int index) {
        return values[index];
    }

}
