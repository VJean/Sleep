package controller;

import javafx.beans.NamedArg;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.SleepItem;
import model.SleepModel;
import model.SleepProfile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;

public class SleepController implements Observer {
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
    private ListView sleepItemsListView;
    @FXML
    private CheckBox amountCheckBox;
    @FXML
    private Spinner amountHourField;
    @FXML
    private Spinner amountMinuteField;


    private SleepModel model = SleepModel.getInstance();
    final private SleepItem editingSleepItem = new SleepItem();
    private ChangeListener<? super LocalDate> beginListener;

    private Stage getStage() {
        return (Stage) rootBorderPane.getScene().getWindow();
    }

    public SleepController() {
    }

    public void initialize() {
        // Date fields
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        beginDatePicker.setValue(yesterday);
        beginHourField.setValueFactory(new LoopingIntegerSpinnerValueFactory(0, 23, 23));
        beginMinuteField.setValueFactory(new LoopingIntegerSpinnerValueFactory(0, 59, 0));
        endDatePicker.setValue(today);
        endHourField.setValueFactory(new LoopingIntegerSpinnerValueFactory(0, 23, 8));
        endMinuteField.setValueFactory(new LoopingIntegerSpinnerValueFactory(0, 59, 30));
        // Custom amount binding
        amountHourField.disableProperty().bind(amountCheckBox.selectedProperty().not());
        amountMinuteField.disableProperty().bind(amountCheckBox.selectedProperty().not());

        // begin datetime listener
        ChangeListener<? super LocalDate> beginListener = (observable, oldValue, newValue) -> {
            LocalDate date = beginDatePicker.getValue();
            int h = (int) beginHourField.getValue();
            int m = (int) beginMinuteField.getValue();
            LocalDateTime d = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), h, m);

            editingSleepItem.setBegin(d);
        };
        beginDatePicker.valueProperty().addListener(beginListener);
        beginHourField.valueProperty().addListener(beginListener);
        beginMinuteField.valueProperty().addListener(beginListener);

        // end datetime listener
        ChangeListener<? super LocalDate> endListener = (observable, oldValue, newValue) -> {
            LocalDate date = endDatePicker.getValue();
            int h = (int) endHourField.getValue();
            int m = (int) endMinuteField.getValue();
            LocalDateTime d = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), h, m);

            editingSleepItem.setEnd(d);
        };
        endDatePicker.valueProperty().addListener(endListener);
        endHourField.valueProperty().addListener(endListener);
        endMinuteField.valueProperty().addListener(endListener);


        aloneCheckBox.selectedProperty().bindBidirectional(this.editingSleepItem.aloneProperty());

        // register to the model
        model.addObserver(this);

        sleepItemsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            SleepItem value = (SleepItem) newValue;

            beginDatePicker.setValue(value.getBegin().toLocalDate());
            beginHourField.getEditor().setText(String.valueOf(value.getBegin().getHour()));
            beginMinuteField.getEditor().setText(String.valueOf(value.getBegin().getMinute()));

            endDatePicker.setValue(value.getEnd().toLocalDate());
            endHourField.getEditor().setText(String.valueOf(value.getEnd().getHour()));
            endMinuteField.getEditor().setText(String.valueOf(value.getEnd().getMinute()));

            amountCheckBox.setSelected(value.hasCustomAmount());
            if (value.hasCustomAmount()) {
                long hours = value.getAmount().getSeconds() / 3600;
                long minutes = (value.getAmount().getSeconds() - hours * 3600) / 60;
                amountHourField.getEditor().setText(String.valueOf(hours));
                amountMinuteField.getEditor().setText(String.valueOf(minutes));
            } else {
                amountHourField.getEditor().clear();
                amountMinuteField.getEditor().clear();
            }
            aloneCheckBox.setSelected(value.getAlone());
            placeChoiceBox.setValue(value.getWhere());

//            this.editingSleepItem.setBegin(value.getBegin());
//            this.editingSleepItem.setEnd(value.getEnd());
//            this.editingSleepItem.setAmount(value.getAmount());
//            this.editingSleepItem.setAlone(value.getAlone());
//            this.editingSleepItem.setWhere(value.getWhere());
        });


    }

    @FXML
    public void loadProfile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open profile");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("json files", "json"));
        File f = fileChooser.showOpenDialog(rootBorderPane.getScene().getWindow());

        model.loadProfile(f);
    }

    @FXML
    public void saveProfile() {
        model.saveProfile();
    }

    @FXML
    public void addSleepItem() {
        Alert alert = new Alert(Alert.AlertType.NONE);

        if (this.model.addItem(this.editingSleepItem)) {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("Item added successfully");
        } else {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Can not add item");
        }

        alert.showAndWait();
    }

    @FXML
    public void close() {

    }

    @Override
    public void update(Observable o, Object arg) {
        SleepProfile sp = (SleepProfile) arg;
        System.out.println("profile changed notification : " + sp.getName());

        getStage().setTitle("Sleep - " + sp.getName());
        placeChoiceBox.setItems(sp.getPlaces());
        sleepItemsListView.setItems(sp.getSleepItems());
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
