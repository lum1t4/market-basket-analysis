package it.uniba.map.marketbasketanlysis.utility;

/**
 * Eccezzione coda vuota
 */
public class EmptyQueueException extends RuntimeException {

    public EmptyQueueException(String message) {
        super(message);
    }

    public EmptyQueueException() {

    }

}
