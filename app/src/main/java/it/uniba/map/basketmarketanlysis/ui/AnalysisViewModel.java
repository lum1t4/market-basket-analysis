package it.uniba.map.basketmarketanlysis.ui;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

import io.reactivex.annotations.NonNull;
import it.uniba.map.basketmarketanlysis.R;
import it.uniba.map.basketmarketanlysis.model.Data;
import it.uniba.map.basketmarketanlysis.model.data.Dataset;
import it.uniba.map.basketmarketanlysis.model.mining.EmergingPatternMiner;
import it.uniba.map.basketmarketanlysis.model.mining.FrequentPatternMiner;
import it.uniba.map.basketmarketanlysis.utility.EmptySetException;


/**
 * ViewModel (controller) per l'activity <code>AnalysisActivity</code>
 * implementa la business logic dell'activity e interegisce con i controller
 */
public class AnalysisViewModel extends AndroidViewModel {

    private final Dataset target;
    private final Dataset background;
    private final MutableLiveData<AnalysisResult> observableContainer = new MutableLiveData<>();

    public AnalysisViewModel(@NonNull Application application, @NonNull Dataset target, @NonNull Dataset background) {
        super(application);
        this.target = target;
        this.background = background;
    }

    /**
     * @return restituisce una contenitore osservabile e consapevole il ciclo di vita
     * dell'activity nella quale aggiorneremo il risultato della analisi
     */
    public LiveData<AnalysisResult> getAnalysisResult() {
        return observableContainer;
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
            observableContainer.setValue(new AnalysisResult(R.string.invalid_support_rate));
            return;
        }

        if (!isValidGrowRate(growRate)) {
            observableContainer.setValue(new AnalysisResult(R.string.invalid_grow_rate));
            return;
        }

        AnalysisResult result;

        try {
            result = loadAnalysis(supportRate, growRate);
            observableContainer.setValue(new AnalysisResult(R.string.loaded_from_cache));
            observableContainer.setValue(result);
        } catch (Exception e) {
            result = computeAnalysis(supportRate, growRate);
            observableContainer.setValue(result);
            Log.e("LOAD", "not found");
        }

    }

    /**
     * Legge dalla memoria secondaria una analisi già effettuato e salvata
     * @param supportRate supporto minimo
     * @param growRate crescita minima
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private AnalysisResult loadAnalysis(float supportRate, float growRate)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        Context context = getApplication().getApplicationContext();
        Pair<FrequentPatternMiner, EmergingPatternMiner> pair;
        String FPName = String.format(
                Locale.getDefault(),
                "FP_TARGET%d_BACKGROUND%d_MIN_SUPPORT%f.cache",
                target.getId(),
                background.getId(),
                supportRate
        );

        String EPName = String.format(
                Locale.getDefault(),
                "EP_TARGET%d_BACKGROUND%d_MIN_SUPPORT%f_MIN_GROW_%f",
                target.getId(),
                background.getId(),
                supportRate,
                growRate
        );

        ObjectInputStream FPStream = new ObjectInputStream(context.openFileInput(FPName));
        ObjectInputStream EPStream = new ObjectInputStream(context.openFileInput(EPName));

        pair = new Pair<>(
                (FrequentPatternMiner) FPStream.readObject(),
                (EmergingPatternMiner) EPStream.readObject()
        );


        return new AnalysisResult(pair);

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
     * @param FPMiner insieme dei pattern frequenti
     * @param EPMiner insieme dei pattern emergenti
     * @param supportRate supporto minimo
     * @param growRate crescita minima
     */
    private void saveAnalysis(FrequentPatternMiner FPMiner, EmergingPatternMiner EPMiner, float supportRate, float growRate) {
        Context context = getApplication().getApplicationContext();
        Pair<FrequentPatternMiner, EmergingPatternMiner> pair;
        String FPName = String.format(
                Locale.getDefault(),
                "FP_TARGET%d_BACKGROUND%d_MIN_SUPPORT%f.cache",
                target.getId(),
                background.getId(),
                supportRate
        );

        String EPName = String.format(
                Locale.getDefault(),
                "EP_TARGET%d_BACKGROUND%d_MIN_SUPPORT%f_MIN_GROW_%f",
                target.getId(),
                background.getId(),
                supportRate,
                growRate
        );

        try (FileOutputStream stream = context.openFileOutput(FPName, Context.MODE_PRIVATE)) {
            ObjectOutputStream FPStream = new ObjectOutputStream(stream);
            FPStream.writeObject(FPMiner);

        } catch (IOException e)  {
            Log.e("SAVE_ANALYSIS_FP", e.toString());
        }

        try (FileOutputStream stream = context.openFileOutput(FPName, Context.MODE_PRIVATE)) {
            ObjectOutputStream EPStream = new ObjectOutputStream(stream);
            EPStream.writeObject(EPMiner);

        } catch (IOException e)  {
            Log.e("SAVE_ANALYSIS_EP", e.toString());
        }


    }
}
