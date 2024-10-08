package it.uniba.map.marketbasketanlysis.model.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


/**
 * Basket rappresenta una riga nel db,
 * un rilevamento/pattern atomico
 */
@Entity(tableName = "baskets")
public class Basket implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "basket_id")
    private int id;
    private String outlook;
    private Float temperature;
    private String humidity;
    private String wind;
    private String play;

    // Foreign key
    private long dataset;

    public Basket(String outlook, Float temperature, String humidity, String wind, String play) {
        this.outlook = outlook;
        this.temperature = temperature;
        this.humidity = humidity;
        this.wind = wind;
        this.play = play;
    }

    public Basket(String outlook, Float temperature, String humidity, String wind, String play, long datasetId) {
        this.outlook = outlook;
        this.temperature = temperature;
        this.humidity = humidity;
        this.wind = wind;
        this.play = play;
        this.dataset = datasetId;
    }

    /**
     * @return identificatore del basket
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'identificatore
     * @param id nuovo identificatore
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return outlook
     */
    public String getOutlook() {
        return outlook;
    }

    /**
     * @param outlook nuovo outlook
     */
    public void setOutlook(String outlook) {
        this.outlook = outlook;
    }

    /**
     * @return temperature
     */
    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public long getDataset() {
        return dataset;
    }

    public void setDataset(long dataset) {
        this.dataset = dataset;
    }

    /**
     * Dato un attributo restituisce il valore presente nel basket
     * corrispondente ad esso
     * @param attribute un attributo
     * @return value valore corrispondente all'attributo
     */
    public Object getValue(Attribute attribute) {
        String name = attribute.getName();
        switch (name) {
            case "Outlook":
                return outlook;
            case "Temperature":
                return temperature;
            case "Humidity":
                return humidity;
            case "Wind":
                return wind;
            case "PlayTennis":
                return play;
            default:
                return "";
        }
    }


    /**
     * @return stringa descrittiva del basket
     */
    @NonNull
    @Override
    public String toString() {
        return "Basket{" +
                "id=" + id +
                ", outlook='" + outlook + '\'' +
                ", temperature=" + temperature +
                ", humidity='" + humidity + '\'' +
                ", wind='" + wind + '\'' +
                ", play='" + play + '\'' +
                ", dataset=" + dataset +
                '}';
    }
}


