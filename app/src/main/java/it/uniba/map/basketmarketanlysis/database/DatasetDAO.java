package it.uniba.map.basketmarketanlysis.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.uniba.map.basketmarketanlysis.model.data.Dataset;

/**
 * Interfaccia a cui la libreria Room fornirà l'implementazione
 * e permetterà di astrarre l'accesso al db dell'entità Dataset
 */
@Dao
public interface DatasetDAO {

    @Insert
    void insertDataset(Dataset dataset);

    @Update
    void updateDataset(Dataset dataset);

    @Delete
    void deleteDataset(Dataset dataset);

    @Query("SELECT * FROM datasets")
    List<Dataset> getDatasets();

    @Query("SELECT * FROM datasets")
    LiveData<List<Dataset>> getAllDatasetsLive();

    @Query("SELECT * FROM datasets WHERE dataset_label = :label LIMIT 1")
    Dataset getDataset(String label);

    @Query("SELECT * FROM datasets WHERE dataset_id = :id LIMIT 1")
    Dataset getDataset(long id);

    @Query("SELECT EXISTS(SELECT 1 FROM datasets WHERE dataset_label = :label LIMIT 1)")
    boolean existsDatabase(String label);

    @Query("SELECT EXISTS(SELECT 1 FROM datasets WHERE dataset_id = :id LIMIT 1)")
    boolean existsDatabase(long id);

}
