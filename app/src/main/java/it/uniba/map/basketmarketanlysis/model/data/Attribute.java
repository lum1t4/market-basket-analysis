package it.uniba.map.basketmarketanlysis.model.data;

import java.io.Serializable;

/**
 * <code>{@code Attribute} rappresenta la colonna/categoria in cui ricadono gli item</code>
 */
public abstract class Attribute implements Serializable {

    private final String name;
    // Column
    private final int index;

    /**
     * Constructor
     *
     * @param name  Nome dell'attributo
     * @param index Indice dell'attributo, impegato nella rappresentazione
     *              fisica nella classe Data
     */
    Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * @return name Nome dell'attributo
     */
    public String getName() {
        return name;
    }

    /**
     * @return indice dell'attributo
     */
    public int getIndex() {
        return index;
    }

    public String toString() {
        return name;
    }

}