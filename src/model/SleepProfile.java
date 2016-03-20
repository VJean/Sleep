package model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;


/**
 * Created by JeanV on 20/03/2016.
 */
public class SleepProfile {
    private StringProperty name = new SimpleStringProperty();
    private ObservableList<String> places = new SimpleListProperty<>();
    private ObservableList<SleepItem> sleepItems = new SimpleListProperty<>();

    public SleepProfile() {
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
    public ObservableList<SleepItem> getSleepItems() {
        return sleepItems;
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
    public ObservableList getPlaces() {
        return places;
    }

    /**
     * places setter
     */
    public void setPlaces(ObservableList places) {
        this.places = places;
    }
}
