package it.uniba.map.marketbasketanlysis.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import it.uniba.map.marketbasketanlysis.R;
import it.uniba.map.marketbasketanlysis.model.data.Dataset;

/**
 * Renderizza la grafica della lista di dataset e gestisce gli eventi collegati ad essa
 */
public class DatasetAdapter extends RecyclerView.Adapter<DatasetAdapter.ViewHolder> {

    private final int MAX_SELECTABLE = 2;
    private final List<DataBinding> source;
    private final List<Dataset> datasets = new ArrayList<>(MAX_SELECTABLE);

    /**
     * Costruttore
     * @param datasets lista dei dataset da visuallizzare
     */
    DatasetAdapter(List<Dataset> datasets) {
        source = datasets.stream()
                .map(DataBinding::new)
                .collect(Collectors.toList());
    }

    /**
     * Crea una view dal layout xml <code>dataset_item.xml</code> e la passa in
     * input al costruttore di ViewHolder
     * @param parent la ViewGroup nel quale la nuova View sarà aggiunta dopo che è
     *               assegnata una posizione a qquest'ultima
     * @param viewType il tip di view della nuova View
     * @return ViewHolder che detiene e gestice una view
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.dataset_item, parent, false);

        return new ViewHolder(view);
    }

    /**
     * Passa i dati dell'elemento in posizione <code>position</code> e li passa al holder
     * della View nel quale si vogliono far comparire i dati
     * @param holder il contenitore della view
     * @param position posizione dell'elemento
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        DataBinding current = source.get(position);
        Dataset dataset = current.getDataset();

        // Filling data
        holder.getTitleView().setText(dataset.getLabel());
        holder.getNumberOfBasketsView().setText(dataset.getNumberOfExamples() + " baskets");
        holder.getContentView().setText(dataset.getBaskets().toString());

        // Initializing UI
        holder.getCheckView().setVisibility(current.isSelected() ? View.VISIBLE : View.GONE);
        holder.getContentWrapperView().setVisibility(current.isExpanded() ? View.VISIBLE : View.GONE);

    }

    /**
     * @return Il numero di elementi da rappresentare
     */
    @Override
    public int getItemCount() {
        return source.size();
    }

    /**
     * Classe che aggiunge due campi al dato per memorizzare se l'elemento è stato selezionato
     * nell'interfaccia e se si visualizzano i dettagli
     */
    private class DataBinding {

        private final Dataset dataset;
        private boolean expanded;
        private boolean selected;

        public DataBinding(Dataset dataset) {
            this.dataset = dataset;
            this.expanded = false;
            this.selected = false;
        }

        /**
         * Nega il campo <code>{@code expanded}</code>
         */
        public void switchExpansion() {
            expanded = !expanded;
        }

        /**
         * @return true se si stanno mostrando i dettagli, false altrimenti
         */
        public boolean isExpanded() {
            return expanded;
        }

        /**
         * @return true se l'elemento è selezionato, false altrimenti
         */
        public boolean isSelected() {
            return selected;
        }

        /**
         * Se l'elemento è già presente nella lista dei selezionati lo rimuove
         * altrimenti aggiunge alla lista l'elemento.
         * In entrambi i casi cambia il valore del campo <code>{@code selected}</code> effettuanto
         * la negozione con il valore precedente
         */
        public void switchSelection() {
            if (selected) {
                datasets.remove(dataset);
            } else {
                datasets.add(dataset);
            }
            selected = !selected;
        }

        /**
         * @return dataset contenuto in questo wrapper
         */
        public Dataset getDataset() {
            return dataset;
        }

    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView noOfBaskets;
        public final View check;
        public final View contentWrapper;
        public final TextView content;
        public final View root;

        /**
         * Contiene una view e ne gestisce il comportamento
         * @param view view da gestire
         */
        public ViewHolder(View view) {
            super(view);
            root = view;
            title = view.findViewById(R.id.dataset_item_label);
            noOfBaskets = view.findViewById(R.id.dataset_item_number_of_examples);

            // Show if an item is selected
            check = view.findViewById(R.id.dataset_item_selected);

            // UI to show dataset content
            contentWrapper = view.findViewById(R.id.dataset_item_content);
            content = view.findViewById(R.id.dataset_item_content_text);


            // Define click listener for the ViewHolder's View

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    DataBinding current = source.get(getAdapterPosition());
                    current.switchSelection();
                    check.setVisibility(current.isSelected() ? View.VISIBLE : View.GONE);


                    if (datasets.size() == MAX_SELECTABLE) {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, AnalysisActivity.class);
                        intent.putExtra("DATASET_TARGET", datasets.get(0));
                        intent.putExtra("DATASET_BACKGROUND", datasets.get(1));
                        context.startActivity(intent);
                    }

                    notifyItemChanged(getAdapterPosition());

                    // true if the onLongClick event is handled by the current view
                    return true;
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataBinding current = source.get(getAdapterPosition());
                    current.switchExpansion();
                    contentWrapper.setVisibility(current.isExpanded() ? View.VISIBLE : View.GONE);
                    notifyItemChanged(getAdapterPosition());


                }
            });

        }

        /**
         * @return view nella quale far comparire il titolo del dataset
         */
        public TextView getTitleView() {
            return title;
        }


        /**
         * @return view nella quale far comparire il numero di basket del dataset
         */
        public TextView getNumberOfBasketsView() {
            return noOfBaskets;
        }

        /**
         * @return view nella quale far comparire una stringa descrittiva dei basket contenuti
         * nel dataset
         */
        public TextView getContentView() {
            return content;
        }

        /**
         * @return view della spunta di selezione
         */
        public View getCheckView() {
            return check;
        }

        /**
         * @return view che conterrà la textview della descrizione del dataset {@link #getContentView()}
         */
        public View getContentWrapperView() {
            return contentWrapper;
        }
    }

}
