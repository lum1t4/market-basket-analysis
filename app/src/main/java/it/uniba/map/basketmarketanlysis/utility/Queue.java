package it.uniba.map.basketmarketanlysis.utility;

/**
 * Fornisce una implementazione della struttara dati coda
 */
public class Queue<T> {
    private Record begin = null;
    private Record end = null;

    /**
     * @return true se la code è vuota
     */
    public boolean isEmpty() {
        return begin == null;
    }


    /**
     * Aggiunge un elemento alla code
     * @param element elemento da aggiungere alla code
     */
    public void enqueue(T element) {
        if (isEmpty())
            begin = end = new Record(element);
        else {
            end.next = new Record(element);
            end = end.next;
        }
    }

    /**
     * @return il primo elemento nella coda
     */
    public T first() {
        return begin.element;
    }

    /**
     * Rimuove il primo elemento della coda
     */
    public void dequeue() {
        if (begin == end) {
            if (begin == null) {
                throw new EmptyQueueException("The queue is empty!");
            } else {
                begin = end = null;
            }
        } else {
            begin = begin.next;
        }
    }

    /**
     * Cella che continete un elemento e il riferimento alla
     * cella successiva
     */
    private class Record {
        T element;
        Record next;

        Record(T element) {
            this.element = element;
            this.next = null;
        }
    }

}