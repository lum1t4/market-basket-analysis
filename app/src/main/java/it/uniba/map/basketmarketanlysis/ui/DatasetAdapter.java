package it.uniba.map.basketmarketanlysis.ui;

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

import it.uniba.map.basketmarketanlysis.R;
import it.uniba.map.basketmarketanlysis.model.data.Dataset;

public class DatasetAdapter extends RecyclerView.Adapter<DatasetAdapter.ViewHolder> {

    private final int MAX_SELECTABLE = 2;
    private final List<DataBinding> source;
    private final List<Dataset> datasets = new ArrayList<>(MAX_SELECTABLE);


    DatasetAdapter(List<Dataset> datasets) {
        source = datasets.stream()
                .map(DataBinding::new)
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.dataset_item, parent, false);

        return new ViewHolder(view);
    }

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

    @Override
    public int getItemCount() {
        return source.size();
    }

    private class DataBinding {

        private final Dataset dataset;
        private boolean expanded;
        private boolean selected;

        public DataBinding(Dataset dataset) {
            this.dataset = dataset;
            this.expanded = false;
            this.selected = false;
        }

        public void switchExpansion() {
            expanded = !expanded;
        }

        public boolean isExpanded() {
            return expanded;
        }

        public boolean isSelected() {
            return selected;
        }

        public void switchSelection() {
            if (selected) {
                datasets.remove(dataset);
            } else {
                datasets.add(dataset);
            }
            selected = !selected;
        }

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

        public TextView getTitleView() {
            return title;
        }

        public TextView getNumberOfBasketsView() {
            return noOfBaskets;
        }

        public TextView getContentView() {
            return content;
        }

        public View getCheckView() {
            return check;
        }

        public View getContentWrapperView() {
            return contentWrapper;
        }
    }

}
