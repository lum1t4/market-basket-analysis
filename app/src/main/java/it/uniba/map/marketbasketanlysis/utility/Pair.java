package it.uniba.map.marketbasketanlysis.utility;

/**
 * Classe che rappresenta una coppia ordinata di valori
 * @param <F> Tipo del primo elemento
 * @param <S> Tipo del secondo elemento
 */
public class Pair<F,S> {
    public final F first;
    public final S second;

    /**
     * Costruttore
     * @param first primo elemento
     * @param second secondo elemento
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

}
