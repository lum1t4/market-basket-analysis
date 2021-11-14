package it.uniba.map.marketbasketanlysis.utility;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Utilliza un executor per far eseguire in un thread separato dei task
 */
public class TaskRunner {

    private final Executor executor;
    private final Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Costruttore
     * @param executor da utillizare per eseguire in background i task
     */
    public TaskRunner(Executor executor) {
        this.executor = executor;
    }

    /**
     * Esegue in background (thread non principale) la funzione (o meglio funtore) supplier e restituisce
     * il risultato ad una funzione consumer eseguita nel thread principale
     * @param supplier funzione che fornisce in output un dato di tip R da eseguire in baskground
     * @param consumer funzione che consuma in dato di tipo R
     * @param <R> tipo dell'oggetto da reperire e consumare
     */
    public <R> void executeAsync(Supplier<R> supplier, Consumer<R> consumer) {
        executor.execute(() -> {
            R result = supplier.get();
            handler.post(() -> {
                consumer.accept(result);
            });
        });
    }

    /**
     * Esegue in background (thread non principale) un runnable
     * @param runnable funtore da eseguire in background
     */
    public void executeAsync(Runnable runnable) {
        executor.execute(runnable);
    }

}