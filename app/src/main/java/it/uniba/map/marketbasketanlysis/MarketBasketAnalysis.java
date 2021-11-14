package it.uniba.map.marketbasketanlysis;

import android.app.Application;

import java.io.File;

import it.uniba.map.marketbasketanlysis.database.AppDatabase;
import it.uniba.map.marketbasketanlysis.database.BasketDAO;
import it.uniba.map.marketbasketanlysis.database.DatasetDAO;
import it.uniba.map.marketbasketanlysis.repository.DatasetRepository;

/**
 * <code>{@code Application}</code> è la classe che viene eseguita per prima all'avvio della applicazione.
 * E' possibile, come in questo caso, specificare una classe derivata per aggiungere valori accessibili
 * globalmente nella applicazione
 */
public class MarketBasketAnalysis extends Application {
    private static DatasetRepository datasetRepository;
    public static final String APP_DATABASE_NAME = "market_basket_analysis_database";
    private static File storage;


    /**
     * In questo metodo viene inizializzato il db e rese disponibili le istanze delle classi
     * repository che dovrebbero essere SSOT (Single source of truth) cioè l'unico modo con cui
     * interfacciarsi con le sorgenti dei dati
     */
    @Override
    public void onCreate() {
        super.onCreate();
        storage = getApplicationContext().getFilesDir();
        // Dependency Injection
        AppDatabase database = AppDatabase.getInstance(getApplicationContext());
        BasketDAO basketDAO = database.basketDAO();
        DatasetDAO datasetDAO = database.datasetDAO();
        datasetRepository = DatasetRepository.getInstance(datasetDAO, basketDAO);
    }

    /**
     * @return l'istanza di <code>{@code DatasetRepository}</code>
     */
    public static DatasetRepository getDatasetRepository() {
        return datasetRepository;
    }


    /**
     * @return la directory nella quale è possibile leggere e scrivere
     *      da parte della applicazione
     */
    public static File getStorage() {
        return storage;
    }

}
