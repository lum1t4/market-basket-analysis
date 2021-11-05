package it.uniba.map.basketmarketanlysis.model.data;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(tableName = "datasets")
public class Dataset implements Iterable<Basket>, Serializable {


    @Ignore
    List<Basket> baskets;
    @Ignore
    List<Attribute> attributes;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "dataset_id")
    private long id;
    @ColumnInfo(name = "dataset_label")
    private String label;

    public Dataset(String label) {
        this.label = label;
    }

    @Ignore
    public Dataset(long id, String label) {
        this.id = id;
        this.label = label;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getNumberOfExamples() {
        return baskets.size();
    }

    public void addBasket(@NonNull Basket basket) {
        if (baskets == null) {
            baskets = new LinkedList<>();
        }
        basket.setDataset(id);
        baskets.add(basket);
    }

    public List<Basket> getBaskets() {
        return baskets;
    }

    public void setBaskets(@NonNull List<Basket> baskets) {
        baskets.forEach(basket -> basket.setDataset(id));
        this.baskets = baskets;
    }

    @NonNull
    @Override
    public Iterator<Basket> iterator() {
        return baskets.iterator();
    }


    @NonNull
    @Override
    public String toString() {
        return baskets.stream()
                .map(Basket::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dataset dataset = (Dataset) o;
        return label.equals(dataset.label) && id == dataset.id && baskets.equals(dataset.baskets);
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
}