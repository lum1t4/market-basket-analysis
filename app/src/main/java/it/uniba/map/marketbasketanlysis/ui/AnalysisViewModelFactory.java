package it.uniba.map.marketbasketanlysis.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.uniba.map.marketbasketanlysis.model.data.Dataset;


/**
 * Le classi ViewModel non sono direttamente istanziabili nella activity
 * ma vengono instanziate da <code>ViewModelProvider</code> che funziona
 * di default solo con costruttori senza parametri. Qualora bisogna specificare
 * dei parametri si deve implentare ViewModelProvider.Factory poichè le classi ViewModel
 * non sono direttamente instanziabili
 */
public class AnalysisViewModelFactory implements ViewModelProvider.Factory {
    private final Dataset target;
    private final Dataset background;

    public AnalysisViewModelFactory(Dataset target, Dataset background) {
        this.target = target;
        this.background = background;
    }


    /**
     * Istanzia un oggetto di tipo AnalysisViewModel
     * @return view model per analysis activity
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AnalysisViewModel.class)) {
            return (T) new AnalysisViewModel(target, background);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
