package it.uniba.map.marketbasketanlysis.database;

import static it.uniba.map.marketbasketanlysis.MarketBasketAnalysis.APP_DATABASE_NAME;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import it.uniba.map.marketbasketanlysis.model.data.Basket;
import it.uniba.map.marketbasketanlysis.model.data.Dataset;

/**
 * La classe <code>AppDatabase</code>, sulla quale viene applicato il design pattern del
 * Sigleton, crea una istanza SQLite e detiene tutti i DAO (Data Access Object),
 * la cui implementazione è fornita al momento della compilazione grazie alla libreria Room
 */
@Database(entities = {Basket.class, Dataset.class}, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    private static final Executor executor = Executors.newSingleThreadExecutor();
    private static volatile AppDatabase instance;

    /**
     * Il modificatore {@code synchronized } è usato al fine di evitare che più thread
     * nello stesso momento utilizzino questo metodo.
     *
     * @param context della applicazione è richiesto al fine di non
     *                creare istanze multiple del Database
     * @return istance di AppDatabase, l'attributo restituito ha tra i modificatori
     * <code>volatile</code> per garantire che il valore si sempre aggiornato fra
     * i thread
     */
    public static synchronized AppDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, APP_DATABASE_NAME)
                    //.fallbackToDestructiveMigration()
                    .createFromAsset("app.db")
                    //.allowMainThreadQueries() // consente di esegure query nel thread principale
                    //.addCallback(roomCallback) // chiamata effettuata al momento di creazione del db
                    .build();
        }

        return instance;
    }


    public static Executor getDatabaseThreadExecutor() {
        return executor;
    }

    /**
     * Al momento della compilazione la libreria, creaerà
     * l'implementazione per questa classe
     * @return dataset Data Access Object
     */
    public abstract DatasetDAO datasetDAO();

    /**
     * Al momento della compilazione la libreria, creaerà
     * l'implementazione per questa classe
     * @return basket Data Access Object
     */
    public abstract BasketDAO basketDAO();


}