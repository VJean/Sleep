package controller;

import javafx.beans.NamedArg;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import model.SleepModel;
import model.SleepProfile;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

public class SleepController implements Observer{
    @FXML
    private BorderPane rootBorderPane;
    @FXML
    private DatePicker beginDatePicker;
    @FXML
    private Spinner beginHourField;
    @FXML
    private Spinner beginMinuteField;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Spinner endHourField;
    @FXML
    private Spinner endMinuteField;
    @FXML
    private CheckBox aloneCheckBox;
    @FXML
    private ChoiceBox placeChoiceBox;
    @FXML
    private Button addButton;

    private SleepModel model = SleepModel.getInstance();


    public SleepController() {
    }

    public void initialize() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        beginDatePicker.setValue(yesterday);
        beginHourField.setValueFactory(new LoopingIntegerSpinnerValueFactory(0, 23, 23));
        beginMinuteField.setValueFactory(new LoopingIntegerSpinnerValueFactory(0, 59, 0));
        endDatePicker.setValue(today);
        endHourField.setValueFactory(new LoopingIntegerSpinnerValueFactory(0, 23, 8));
        endMinuteField.setValueFactory(new LoopingIntegerSpinnerValueFactory(0, 59, 30));

        // register to the model
        model.addObserver(this);
    }

    public void loadProfile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open profile");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("json files", "json"));
        File f = fileChooser.showOpenDialog(rootBorderPane.getScene().getWindow());

        model.loadProfile(f);
    }

    public void saveProfile() {
        model.saveProfile();
    }

    public void addSleepItem(){

    }

    @Override
    public void update(Observable o, Object arg) {
        SleepProfile sp = (SleepProfile) arg;
        System.out.println("profile changed notification : " + sp.getName());

        placeChoiceBox.setItems(sp.getPlaces());
    }

    /**
     * An IntegerSpinnerValueFactory that loops on its value list :
     * - pressing the upArrow of the control when its value is already at max sets it to min;
     * - pressing the downArrow of the control when its value is already at min sets it to max
     */
    private class LoopingIntegerSpinnerValueFactory extends SpinnerValueFactory.IntegerSpinnerValueFactory {

        public LoopingIntegerSpinnerValueFactory(@NamedArg("min") int min, @NamedArg("max") int max) {
            super(min, max);
        }

        LoopingIntegerSpinnerValueFactory(@NamedArg("min") int min, @NamedArg("max") int max, @NamedArg("initialValue") int initialValue) {
            super(min, max, initialValue);
        }

        @Override
        public void decrement(int steps) {
            if (getValue() - steps < getMin())
                setValue(getMax() - steps - getValue() - getMin() + 1);
            else
                super.decrement(steps);
        }

        @Override
        public void increment(int steps) {
            if (getValue() + steps > getMax())
                setValue(getMin() + steps - getMax() - getValue() - 1);
            else
                super.increment(steps);
        }
    }
}
