package it.uniba.map.marketbasketanlysis.utility;


/**
 * Eccezzione lanciata qualora l'insieme sul quale si effettua l'operazione
 * risulti vuoto
 */
public class EmptySetException extends Exception {

    public EmptySetException() {

    }

    public EmptySetException(String msg) {
        super(msg);
    }
}
