package it.uniba.map.marketbasketanlysis.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import it.uniba.map.marketbasketanlysis.R;
import it.uniba.map.marketbasketanlysis.model.data.Dataset;
import it.uniba.map.marketbasketanlysis.model.mining.EmergingPatternMiner;
import it.uniba.map.marketbasketanlysis.model.mining.FrequentPatternMiner;
import it.uniba.map.marketbasketanlysis.utility.Pair;

/**
 * Activity secondaria nella quale l'utente dati i due dataset, precedentemente selezionati,
 * supportRate e growRate inseriti in input tramite il form mostra i frequent pattern
 * e gli emerging pattern, i quali vengono calcolati oppure reperiti dalla memoria secodaria
 * se il calcolo è stato già effettuato
 */
public class AnalysisActivity extends AppCompatActivity {

    private AnalysisViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        // Referenziare l'interfaccia
        EditText supportRateInput = findViewById(R.id.supportRateEditNumber);
        EditText growRateEditInput = findViewById(R.id.growRateEditNumber);
        TextView frequentPatternView = findViewById(R.id.frequent_pattern_view);
        TextView emergingPatternView = findViewById(R.id.emerging_pattern_view);
        Button button = findViewById(R.id.computeButton);

        // Cattura intent
        Intent intent = getIntent();
        Dataset datasetTarget = (Dataset) intent.getSerializableExtra("DATASET_TARGET");
        Dataset datasetBackground = (Dataset) intent.getSerializableExtra("DATASET_BACKGROUND");

        AnalysisViewModelFactory factory = new AnalysisViewModelFactory(datasetTarget, datasetBackground);
        viewModel = new ViewModelProvider(this, factory).get(AnalysisViewModel.class);

        viewModel.getAnalysisResult().observe(this, new Observer<AnalysisResult>() {
            @Override
            public void onChanged(AnalysisResult analysisResult) {
                if (analysisResult == null) {
                    return;
                }

                if (analysisResult.getError() != null) {
                    CharSequence text = getString(analysisResult.getError());
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }

                Pair<FrequentPatternMiner, EmergingPatternMiner> success = analysisResult.getSuccess();
                if (success != null) {
                    frequentPatternView.setText(success.first.toString());
                    emergingPatternView.setText(success.second.toString());
                }

            }
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean enable = supportRateInput.getText().toString().length() > 0
                        && growRateEditInput.getText().toString().length() > 0;
                button.setEnabled(enable);
            }
        };

        supportRateInput.addTextChangedListener(watcher);
        growRateEditInput.addTextChangedListener(watcher);

        // Aggiunge listener al bottone del form
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float support = Float.parseFloat(supportRateInput.getText().toString());
                float grow = Float.parseFloat(growRateEditInput.getText().toString());
                frequentPatternView.setText("");
                emergingPatternView.setText("");
                viewModel.retrieveAnalysis(support, grow);
            }
        });

    }

}