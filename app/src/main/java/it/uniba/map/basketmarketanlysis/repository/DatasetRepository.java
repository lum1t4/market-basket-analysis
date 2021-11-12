package it.uniba.map.basketmarketanlysis.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import it.uniba.map.basketmarketanlysis.database.AppDatabase;
import it.uniba.map.basketmarketanlysis.database.BasketDAO;
import it.uniba.map.basketmarketanlysis.database.DatasetDAO;
import it.uniba.map.basketmarketanlysis.model.data.Attribute;
import it.uniba.map.basketmarketanlysis.model.data.ContinuousAttribute;
import it.uniba.map.basketmarketanlysis.model.data.Dataset;
import it.uniba.map.basketmarketanlysis.model.data.DiscreteAttribute;
import it.uniba.map.basketmarketanlysis.utility.TaskRunner;

/**
 * Le classi Repository secondo il pattern MVVC sviluppano
 * la business logic, gestiscono le operazioni CRUD attraverso i DAO
 * (Data Access Object) da diverse sorgenti (remoto, database locale, cache...)
 * astraendo cosi l'accesso all'informazione da diverse fonti. La documentazione ufficiale
 * afferma che queste debbano essere SSOT (single source of truth).
 */
public class DatasetRepository {

    private static volatile DatasetRepository instance;
    private final DatasetDAO datasetDAO;
    private final BasketDAO basketDAO;
    private final TaskRunner runner = new TaskRunner(AppDatabase.getDatabaseThreadExecutor());

    /**
     * Costruttore privato per impedire che siano instaziabili più oggetti di questa classe
     * @param dDAO implementazione dell'interfaccia DatasetDAO
     *             che permette di astrarre le operazioni più elementari del tipo dataset con il db
     * @param bDAO implementazione dell'interfaccia DatasetDAO
     *             che permette di astrarre le operazioni più elementari del tipo basket con il db
     */
    private DatasetRepository(DatasetDAO dDAO, BasketDAO bDAO) {
        datasetDAO = dDAO;
        basketDAO = bDAO;
    }

    /**
     * @param dDAO data access object per l'entity dataset
     * @param bDAO data access object per l'entity basket
     * @return l'unica istanza di DatasetRepository
     */
    public static synchronized DatasetRepository getInstance(DatasetDAO dDAO, BasketDAO bDAO) {
        if (instance == null) {
            instance = new DatasetRepository(dDAO, bDAO);
        }
        return instance;
    }

    /**
     * Inserisce un nuovo dataset nel db
     * @param dataset da aggiungere nel db
     */
    public void insertDataset(Dataset dataset) {
        runner.executeAsync(
                () -> {
                    datasetDAO.insertDataset(dataset);
                    basketDAO.insertBaskets(dataset.getBaskets());
                }
        );
    }

    /**
     * Aggiorna i valori di un dataset nel db
     * @param dataset da aggiornare nel db
     */
    public void updateDataset(Dataset dataset) {
        runner.executeAsync(
                () -> {
                    datasetDAO.updateDataset(dataset);
                    basketDAO.updateBaskets(dataset.getBaskets());
                }
        );
    }

    /**
     * Elimina un dataset dal database in maniera asincrona
     * @param dataset
     */
    public void deleteDataset(Dataset dataset) {
        runner.executeAsync(
                () -> {
                    basketDAO.deleteBaskets(dataset.getBaskets());
                    datasetDAO.deleteDataset(dataset);
                }
        );
    }

    /**
     * Restituisce un dataset effettuando una query con l'indentificatore
     * del dataset
     * @param id identificatore del dataset
     * @return dataset con identificatore id
     */
    public Dataset getDataset(long id) {
        Dataset dataset = datasetDAO.getDataset(id);
        dataset.setBaskets(basketDAO.getBasketsByDataset(id));
        return dataset;
    }

    /**
     * Restituisce un dataset effettuando una query con il nome del
     * dataset
     * @param label nome del dataset
     * @return dataset con nome label
     */
    public Dataset getDataset(String label) {
        Dataset dataset = datasetDAO.getDataset(label);
        dataset.setBaskets(basketDAO.getBasketsByDataset(dataset.getId()));
        return dataset;
    }

    /**
     * Restiutuisce un contenitore osservabile utile e in un thread separato legge i dataset dal
     * database e nel completa il caricamento con <code>{@code lazyLoadDataset}</code>
     * @return liveDataDatasets <code>{@code LiveData<List<Dataset>>}</code> contenitore osservabile
     */
    public LiveData<List<Dataset>> getDatasets() {
        MutableLiveData<List<Dataset>> liveDataDatasets = new MutableLiveData<>();
        runner.executeAsync(
                () -> {
                    List<Dataset> retrieved = datasetDAO.getDatasets();
                    retrieved.forEach(this::lazyLoadDataset);
                    liveDataDatasets.postValue(retrieved);
                }
        );
        return liveDataDatasets;
    }

    /**
     * Completa i campi del dataset che non sono immediatamente
     * disponibili dalla rappresentazione nel database
     * @param dataset da completare
     */
    private void lazyLoadDataset(Dataset dataset) {
        dataset.setBaskets(basketDAO.getBasketsByDataset(dataset.getId()));
        dataset.setAttributes(getAttributes(dataset));
    }

    /**
     * @param dataset da cui estrarre i valori
     * @return lista degli attributi completi dei valori presenti nel dataset
     */
    private List<Attribute> getAttributes(Dataset dataset) {
        List<Attribute> attributes = new ArrayList<>();
        long datasetId = dataset.getId();
        float minTemp = basketDAO.minTemperature(datasetId);
        float maxTemp = basketDAO.maxTemperature(datasetId);
        attributes.add(new DiscreteAttribute("Outlook", 0, basketDAO.getDistinctOutlook(datasetId)));
        attributes.add(new ContinuousAttribute("Temperature", 1, minTemp, maxTemp));
        attributes.add(new DiscreteAttribute("Humidity", 2, basketDAO.getDistinctHumidity(datasetId)));
        attributes.add(new DiscreteAttribute("Wind", 3, basketDAO.getDistinctWind(datasetId)));
        attributes.add(new DiscreteAttribute("PlayTennis", 4, basketDAO.getDistinctPlayTennis(datasetId)));
        return attributes;
    }

}