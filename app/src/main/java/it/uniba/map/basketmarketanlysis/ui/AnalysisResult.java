package it.uniba.map.basketmarketanlysis.ui;

import android.util.Pair;

import androidx.annotation.Nullable;

import it.uniba.map.basketmarketanlysis.model.mining.EmergingPatternMiner;
import it.uniba.map.basketmarketanlysis.model.mining.FrequentPatternMiner;


/**
 * Classe che veicola i messaggi sul risultato dell'analisi tra ViewModel (controller) e l'activty
 * per consentire la separazione di responsabilità tra le due classi
 */
public class AnalysisResult {

    @Nullable
    private Pair<FrequentPatternMiner, EmergingPatternMiner> success;
    @Nullable
    private Integer error;

    /**
     * Costruttore per un risultato dell'analisi corretto
     * @param success coppia (fpMiner, epMiner)
     */
    public AnalysisResult(@Nullable Pair<FrequentPatternMiner, EmergingPatternMiner> success) {
        this.success = success;
    }

    /**
     * Costruttore per un risultato che deve descrivere un errore
     * @param error identificatore della stringa/risorsa che descrive l'errore
     */
    public AnalysisResult(@Nullable Integer error) {
        this.error = error;
    }

    /**
     * @return coppia (fpMiner, epMiner) se il risultato è corretto,
     * altrimenti un valore nullo
     */
    @Nullable
    public Pair<FrequentPatternMiner, EmergingPatternMiner> getSuccess() {
        return success;
    }

    /**
     * @return error di tipo intero se l'analisi non ha fornito un risultato corretto
     *  un valore nullo se l'analisi non ha fornito un risultato corretto
     */
    @Nullable
    public Integer getError() {
        return error;
    }
}
