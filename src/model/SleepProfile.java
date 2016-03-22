package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;


/**
 * Created by JeanV on 20/03/2016.
 */
@JsonSerialize(using = SleepProfileSerializer.class)
@JsonDeserialize(using = SleepProfileDeserializer.class)
public class SleepProfile {
    private StringProperty name = new SimpleStringProperty();
    private ObservableList<String> places = new SimpleListProperty<>();
    private ObservableList<SleepItem> sleepItems = new SimpleListProperty<>();

    public SleepProfile() {
    }

    public SleepProfile(String name, ArrayList<String> places, ArrayList<SleepItem> sleepItems) {
        this.name.setValue(name);
        this.places = FXCollections.observableArrayList(places);
        this.sleepItems = FXCollections.observableArrayList(sleepItems);
    }

    /**
     * name value getter
     */
    public String getName() {
        return name.get();
    }

    /**
     * name property getter
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * name value setter
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * sleepItems getter
     */
    @JsonIgnore
    public ObservableList<SleepItem> getSleepItems() {
        return sleepItems;
    }

    @JsonProperty("sleepItems")
    public SleepItem[] getSleepItemsAsArray(){
        return (SleepItem[]) sleepItems.toArray();
    }

    /**
     * sleepItems setter
     */
    public void setSleepItems(ObservableList<SleepItem> sleepItems) {
        this.sleepItems = sleepItems;
    }

    /**
     * places getter
     */
    @JsonIgnore
    public ObservableList<String> getPlaces() {
        return places;
    }

    @JsonProperty("places")
    public String[] getPlacesAsArray(){
        return (String[]) places.toArray();
    }

    /**
     * places setter
     */
    public void setPlaces(ObservableList<String> places) {
        this.places = places;
    }
}