package it.uniba.map.basketmarketanlysis.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.uniba.map.basketmarketanlysis.R;
import it.uniba.map.basketmarketanlysis.model.data.Dataset;
import it.uniba.map.basketmarketanlysis.repository.DatasetRepository;

/**
 * Activity principale dell'applicazione
 * mostra i dataset disponibili e permemtte di selezionarli
 * e passare all'activity successiva
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.datasets_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        DatasetRepository repository = DatasetRepository.getInstance(getApplicationContext());

        repository.getDatasets().observe(this, new Observer<List<Dataset>>() {
            @Override
            public void onChanged(List<Dataset> datasets) {
                recyclerView.setAdapter(new DatasetAdapter(datasets));
            }
        });

    }

}