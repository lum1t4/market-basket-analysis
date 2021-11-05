package it.uniba.map.basketmarketanlysis.ui;

import android.util.Pair;

import androidx.annotation.Nullable;

import it.uniba.map.basketmarketanlysis.model.mining.EmergingPatternMiner;
import it.uniba.map.basketmarketanlysis.model.mining.FrequentPatternMiner;


/**
 * Classe che veicola i messaggi tra ViewModel (controller) e l'activty
 * per consentire la separazione di responsabilità tra le due classi
 */
public class AnalysisResult {

    @Nullable
    private Pair<FrequentPatternMiner, EmergingPatternMiner> success;
    @Nullable
    private Integer error;

    public AnalysisResult(@Nullable Pair<FrequentPatternMiner, EmergingPatternMiner> success) {
        this.success = success;
    }

    public AnalysisResult(@Nullable Integer error) {
        this.error = error;
    }

    @Nullable
    public Pair<FrequentPatternMiner, EmergingPatternMiner> getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
