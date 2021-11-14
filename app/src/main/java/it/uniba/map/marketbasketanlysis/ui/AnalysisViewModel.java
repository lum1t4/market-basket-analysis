package it.uniba.map.marketbasketanlysis.ui;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

import io.reactivex.annotations.NonNull;
import it.uniba.map.marketbasketanlysis.MarketBasketAnalysis;
import it.uniba.map.marketbasketanlysis.R;
import it.uniba.map.marketbasketanlysis.model.Data;
import it.uniba.map.marketbasketanlysis.model.data.Dataset;
import it.uniba.map.marketbasketanlysis.model.mining.EmergingPatternMiner;
import it.uniba.map.marketbasketanlysis.model.mining.FrequentPatternMiner;
import it.uniba.map.marketbasketanlysis.utility.EmptySetException;
import it.uniba.map.marketbasketanlysis.utility.Pair;


/**
 * ViewModel (controller) per l'activity <code>AnalysisActivity</code>
 * implementa la business logic dell'activity e interegisce con i controller
 */
public class AnalysisViewModel extends ViewModel {

    private final Dataset target;
    private final Dataset background;
    private final MutableLiveData<AnalysisResult> observable = new MutableLiveData<>();

    public AnalysisViewModel(@NonNull Dataset target, @NonNull Dataset background) {
        this.target = target;
        this.background = background;
    }

    /**
     * @return restituisce una contenitore osservabile e che considera
     * il ciclo di vita dell'activity nella quale aggiorneremo
     * il risultato della analisi
     */
    public LiveData<AnalysisResult> getAnalysisResult() {
        return observable;
    }

    /**
     * Metodo che controlla se il valore del support rate è corretto
     *
     * @param supportRate supporto minimo
     * @return <code>true</code> se il support rate è compreso tra 0 e 1, <code>false</code> altrimenti
     */
    public boolean isValidSupportRate(float supportRate) {
        return supportRate > 0 && supportRate <= 1;
    }

    /**
     * Metodo che controlla se il valore del grow rate è corretto
     *
     * @param growRate float
     * @return <code>true</code> se il grow rate è positivo, <code>false</code> altrimenti
     */
    public boolean isValidGrowRate(float growRate) {
        return growRate >= 0;
    }


    /**
     * Effettua il calcolo oppure recupera da file una analisi già effettuata (cache)
     * aggiornando il valore del contenitore osservabile con un risultato (<code>{@code AnalysisResult}</code>)
     * @param supportRate supporto minimo
     * @param growRate creascita minima
     */
    public void retrieveAnalysis(float supportRate, float growRate) {

        if (!isValidSupportRate(supportRate)) {
            observable.setValue(new AnalysisResult(R.string.invalid_support_rate));
            return;
        }

        if (!isValidGrowRate(growRate)) {
            observable.setValue(new AnalysisResult(R.string.invalid_grow_rate));
            return;
        }

        AnalysisResult result;

        try {
            result = loadAnalysis(supportRate, growRate);
            observable.setValue(new AnalysisResult(R.string.loaded_from_cache));
            observable.setValue(result);
        } catch (Exception e) {
            result = computeAnalysis(supportRate, growRate);
            observable.setValue(result);
            Log.e(getClass().toString(), e.toString());
        }

    }

    /**
     *  Legge dalla memoria secondaria una analisi già effettuata e salvata
     * @param supportRate supporto minimo
     * @param growRate crescita minima
     * @return il risultato di una analisi precedentemente salvata
     * @throws IOException non riesce a leggere un file
     * @throws ClassNotFoundException non riconosce un oggetto nello stream
     * @throws ClassCastException i valori salvati sono di un altro tipo
     */
    private AnalysisResult loadAnalysis(float supportRate, float growRate)
            throws IOException, ClassNotFoundException, ClassCastException {

        String path = MarketBasketAnalysis.getStorage().getAbsolutePath();
        String name;
        FileInputStream stream;
        ObjectInputStream objStream;


        FrequentPatternMiner fpMiner;

        name = encodeFPName(supportRate);
        stream = new FileInputStream(new File(path, name));
        objStream = new ObjectInputStream(stream);
        fpMiner = (FrequentPatternMiner) objStream.readObject();
        objStream.close();
        stream.close();

        EmergingPatternMiner epMiner;

        name = encodeEPName(supportRate, growRate);
        stream = new FileInputStream(new File(path, name));
        objStream = new ObjectInputStream(stream);
        epMiner = (EmergingPatternMiner) objStream.readObject();
        objStream.close();
        stream.close();

        return new AnalysisResult(new Pair<>(fpMiner, epMiner));

    }


    /**
     * Effettua il calcolo sui dataset target e background e restituisce un risultato
     * @param supportRate supporto minimo
     * @param growRate creascita minima
     * @return analysisResult contiene, se l'analisi è stata effettuato correttamente, la coppia
     * (fpMiner, epMiner) oppure l'identificatore (un intero) di un risorsa di testo
     * che descrive l'errore durante l'analisi
     */
    private AnalysisResult computeAnalysis(float supportRate, float growRate) {
        Data dataTarget = new Data(target);
        Data dataBackground = new Data(background);

        FrequentPatternMiner fpMiner;
        EmergingPatternMiner epMiner;

        try {
            fpMiner = new FrequentPatternMiner(dataTarget, supportRate);
            try {
                epMiner = new EmergingPatternMiner(dataBackground, fpMiner, growRate);
                saveAnalysis(fpMiner, epMiner, supportRate, growRate);
                return new AnalysisResult(new Pair<>(fpMiner, epMiner));

            } catch (EmptySetException exception) {
                return new AnalysisResult(R.string.emerging_pattern_miner_error);
            }

        } catch (EmptySetException exception) {
            return new AnalysisResult(R.string.frequent_pattern_miner_error);
        }

    }


    /**
     * Salva una analisi sulla memoria secondaria
     * @param fpMiner insieme dei pattern frequenti
     * @param epMiner insieme dei pattern emergenti
     * @param supportRate supporto minimo
     * @param growRate crescita minima
     */
    private void saveAnalysis(FrequentPatternMiner fpMiner, EmergingPatternMiner epMiner, float supportRate, float growRate) {

        File dir = MarketBasketAnalysis.getStorage();

        String name = encodeFPName(supportRate);

        try (FileOutputStream stream = new FileOutputStream(new File(dir.getPath(), name))) {
            ObjectOutputStream FPStream = new ObjectOutputStream(stream);
            FPStream.writeObject(fpMiner);
            FPStream.close();
        } catch (IOException e)  {
            Log.e(getClass().toString(), e.toString());
        }

        name = encodeEPName(supportRate, growRate);

        try (FileOutputStream stream = new FileOutputStream(new File(dir.getPath(), name))) {
            ObjectOutputStream EPStream = new ObjectOutputStream(stream);
            EPStream.writeObject(epMiner);
            EPStream.close();
        } catch (IOException e)  {
            Log.e(getClass().toString(), e.toString());
        }


    }

    private String encodeFPName(float supportRate) {
        return String.format(
                Locale.getDefault(),
                "FP_TARGET%d_BACKGROUND%d_MIN_SUPPORT%f.cache",
                target.getId(),
                background.getId(),
                supportRate
        );
    }

    private String encodeEPName(float supportRate, float growRate) {
        return String.format(
                Locale.getDefault(),
                "EP_TARGET%d_BACKGROUND%d_MIN_SUPPORT%f_MIN_GROW_%f.cache",
                target.getId(),
                background.getId(),
                supportRate,
                growRate
        );
    }
}
