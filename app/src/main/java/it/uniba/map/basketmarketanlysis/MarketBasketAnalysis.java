package it.uniba.map.basketmarketanlysis;

import android.app.Application;

import it.uniba.map.basketmarketanlysis.database.AppDatabase;
import it.uniba.map.basketmarketanlysis.database.BasketDAO;
import it.uniba.map.basketmarketanlysis.database.DatasetDAO;
import it.uniba.map.basketmarketanlysis.repository.DatasetRepository;

/**
 * <code>{@code Application}</code> è la classe che viene eseguita per prima all'avvio della applicazione.
 * E' possibile, come in questo caso, specificare una classe derivata per aggiungere valori
 *
 */
public class MarketBasketAnalysis extends Application {
    private static DatasetRepository datasetRepository;
    public static final String APP_DATABASE_NAME = "market_basket_analysis_database";

    @Override
    public void onCreate() {
        super.onCreate();
        // Dependency Injection
        AppDatabase database = AppDatabase.getInstance(getApplicationContext());
        BasketDAO basketDAO = database.basketDAO();
        DatasetDAO datasetDAO = database.datasetDAO();
        datasetRepository = DatasetRepository.getInstance(datasetDAO, basketDAO);
    }

    public static DatasetRepository getDatasetRepository() {
        return datasetRepository;
    }
}
