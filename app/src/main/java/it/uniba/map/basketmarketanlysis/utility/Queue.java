package it.uniba.map.basketmarketanlysis.utility;

/**
 * Fornisce una implementazione della struttara dati coda
 */
public class Queue<T> {
    private Record begin = null;
    private Record end = null;

    public boolean isEmpty() {
        return begin == null;
    }

    public void enqueue(T element) {
        if (isEmpty())
            begin = end = new Record(element);
        else {
            end.next = new Record(element);
            end = end.next;
        }
    }

    public T first() {
        return begin.element;
    }

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

    private class Record {
        T element;
        Record next;

        Record(T element) {
            this.element = element;
            this.next = null;
        }
    }

}