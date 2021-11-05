package it.uniba.map.basketmarketanlysis.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.uniba.map.basketmarketanlysis.model.data.Basket;

/**
 * Interfaccia a cui la libreria Room fornirà l'implementazione
 * e permetterà di astrarre l'accesso al db dell'entità Basket
 */
@Dao
public interface BasketDAO {

    @Insert
    void insertBasket(Basket basket);

    @Insert
    void insertBaskets(List<Basket> baskets);

    @Delete
    void deleteBasket(Basket basket);

    @Delete
    void deleteBaskets(List<Basket> baskets);

    @Update
    void updateBasket(Basket basket);

    @Update
    void updateBaskets(List<Basket> basket);

    @Query("SELECT * FROM baskets")
    List<Basket> getAllBaskets();

    @Query("SELECT * FROM baskets WHERE dataset = :datasetId")
    List<Basket> getBasketsByDataset(long datasetId);

    @Query("SELECT MIN(temperature) FROM baskets WHERE dataset = :datasetId GROUP BY dataset")
    float minTemperature(long datasetId);

    @Query("SELECT MAX(temperature) FROM baskets WHERE dataset = :datasetId GROUP BY dataset")
    float maxTemperature(long datasetId);

    @Query("SELECT DISTINCT outlook FROM baskets WHERE dataset = :datasetId")
    String[] getDistinctOutlook(long datasetId);

    @Query("SELECT DISTINCT humidity FROM baskets WHERE dataset = :datasetId")
    String[] getDistinctHumidity(long datasetId);

    @Query("SELECT DISTINCT wind FROM baskets WHERE dataset = :datasetId")
    String[] getDistinctWind(long datasetId);

    @Query("SELECT DISTINCT play FROM baskets WHERE dataset = :datasetId")
    String[] getDistinctPlayTennis(long datasetId);


}