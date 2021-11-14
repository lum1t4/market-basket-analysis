package it.uniba.map.marketbasketanlysis.model.mining;

import it.uniba.map.marketbasketanlysis.model.data.DiscreteAttribute;


/**
 * Item che afferisce ad un attributo di tipo discreto
 */
public class DiscreteItem extends Item {

    public DiscreteItem(DiscreteAttribute attribute, String value) {
        super(attribute, value);
    }

    @Override
    public boolean checkItemCondition(Object value) {
        return getValue().equals(value);
    }

}
