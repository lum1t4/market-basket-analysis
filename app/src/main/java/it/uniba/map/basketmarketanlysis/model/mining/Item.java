package it.uniba.map.basketmarketanlysis.model.mining;

import java.io.Serializable;

import it.uniba.map.basketmarketanlysis.model.data.Attribute;

public abstract class Item implements Serializable {

    protected Attribute attribute;
    protected Object value;

    public Item(Attribute attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public Object getValue() {
        return value;
    }

    abstract boolean checkItemCondition(Object value);

    public String toString() {
        return attribute.toString() + "=" + value.toString();
    }

}
