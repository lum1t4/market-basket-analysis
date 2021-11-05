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
 * astraendo cosi l'accesso all'informazione da diverse fonti.
 */
public class DatasetRepository {

    private static volatile DatasetRepository instance;
    private final DatasetDAO datasetDAO;
    private final BasketDAO basketDAO;
    private final TaskRunner runner = new TaskRunner(AppDatabase.getDatabaseThreadExecutor());


    private DatasetRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        datasetDAO = database.datasetDAO();
        basketDAO = database.basketDAO();
    }

    public static DatasetRepository getInstance(Context context) {
        if (instance == null) {
            instance = new DatasetRepository(context);
        }
        return instance;
    }

    public void insertDataset(Dataset dataset) {
        runner.executeAsync(
                () -> {
                    datasetDAO.insertDataset(dataset);
                    basketDAO.insertBaskets(dataset.getBaskets());
                }
        );
    }

    public void updateDataset(Dataset dataset) {
        runner.executeAsync(
                () -> {
                    datasetDAO.updateDataset(dataset);
                    basketDAO.updateBaskets(dataset.getBaskets());
                }
        );
    }

    public void deleteDataset(Dataset dataset) {

        basketDAO.deleteBaskets(dataset.getBaskets());
        datasetDAO.deleteDataset(dataset);
    }

    public Dataset getDataset(long id) {
        Dataset dataset = datasetDAO.getDataset(id);
        dataset.setBaskets(basketDAO.getBasketsByDataset(id));
        return dataset;
    }

    public Dataset getDataset(String label) {
        Dataset dataset = datasetDAO.getDataset(label);
        dataset.setBaskets(basketDAO.getBasketsByDataset(dataset.getId()));
        return dataset;
    }

    public LiveData<List<Dataset>> getDatasets() {
        MutableLiveData<List<Dataset>> dataset = new MutableLiveData<>();
        runner.executeAsync(
                () -> {
                    List<Dataset> retrieved = datasetDAO.getDatasets();
                    retrieved.forEach(this::lazyLoadDataset);
                    dataset.postValue(retrieved);
                }
        );
        return dataset;
    }

    private void lazyLoadDataset(Dataset dataset) {
        dataset.setBaskets(basketDAO.getBasketsByDataset(dataset.getId()));
        dataset.setAttributes(getAttributes(dataset));
    }


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