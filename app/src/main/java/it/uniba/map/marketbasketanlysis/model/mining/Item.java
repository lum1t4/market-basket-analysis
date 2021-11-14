package it.uniba.map.marketbasketanlysis.model.mining;

import java.io.Serializable;

import it.uniba.map.marketbasketanlysis.model.data.Attribute;


/**
 * Elemento atomico che costituisce i pattern
 */
public abstract class Item implements Serializable {

    protected Attribute attribute;
    protected Object value;

    public Item(Attribute attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * @return attributo dell'item
     */
    public Attribute getAttribute() {
        return attribute;
    }


    /**
     * @return valore dell'item
     */
    public Object getValue() {
        return value;
    }

    /**
     *
     * @param value di cui controllare se è valido per l'item
     * @return true se la condizione è verificata, false altrimenti
     */
    abstract boolean checkItemCondition(Object value);

    /**
     * @return stringa di rappresentazione dell'item
     */
    public String toString() {
        return attribute.toString() + "=" + value.toString();
    }

}
