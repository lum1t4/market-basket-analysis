package it.uniba.map.basketmarketanlysis.utility;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class TaskRunner {

    private final Executor executor;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public TaskRunner(Executor executor) {
        this.executor = executor;
    }

    public <R> void executeAsync(Supplier<R> supplier, Consumer<R> consumer) {
        executor.execute(() -> {
            R result = supplier.get();
            handler.post(() -> {
                consumer.accept(result);
            });
        });
    }

    public void executeAsync(Runnable runnable) {
        executor.execute(runnable);
    }

}