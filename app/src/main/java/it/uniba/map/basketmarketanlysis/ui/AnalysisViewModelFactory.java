package it.uniba.map.basketmarketanlysis.ui;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.uniba.map.basketmarketanlysis.model.data.Dataset;
import it.uniba.map.basketmarketanlysis.repository.DatasetRepository;


/**
 * Le classi ViewModel non sono direttamente istanziabili nella activity
 * ma vengono instanziate da <code>ViewModelProvider</code> che funziona
 * di default solo con costruttori senza parametri. Qualora bisogna specificare
 * dei parametri si deve implentare ViewModelProvider.Factory poichè le classi ViewModel
 * non sono direttamente instanziabili
 */
public class AnalysisViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
    private final Dataset target;
    private final Dataset background;
    private Application application;

    public AnalysisViewModelFactory(Application application, Dataset target, Dataset background) {
        super(application);
        this.application = application;
        this.target = target;
        this.background = background;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AnalysisViewModel.class)) {
            return (T) new AnalysisViewModel(application, target, background);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
