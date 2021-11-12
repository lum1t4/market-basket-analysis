package it.uniba.map.basketmarketanlysis.model.data;

/**
 * Classe degli attributi discreti
 */
public class DiscreteAttribute extends Attribute {
    private final String[] values;

    public DiscreteAttribute(String name, int index, String[] values) {
        super(name, index);
        this.values = values;
    }

    /**
     * @return numero di valori che rientrano nell'attributo
     */
    public int getNumberOfDistinctValues() {
        return values.length;
    }

    /**
     * @param index intero che indica la posizione
     *              di un valore
     * @return un valore in posizione index
     */
    public String getValue(int index) {
        return values[index];
    }

}
