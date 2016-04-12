package controller;

import javafx.beans.NamedArg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.SleepItem;
import model.SleepModel;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Created by JeanV on 10/04/2016.
 */
public class FormController extends DialogPane implements Initializable {
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
    private ComboBox<String> placeChoiceBox;
    @FXML
    private CheckBox amountCheckBox;
    @FXML
    private Spinner amountHourField;
    @FXML
    private Spinner amountMinuteField;
    @FXML
    private Button btOk;

    private SleepModel model = SleepModel.getInstance();
    final private SleepItem sleepItem = new SleepItem();

    public FormController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("form-view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buildFields();
        addFormBindings();
    }

    private void buildFields() {
        // time fields
        beginHourField.setValueFactory(new LoopingIntegerSpinnerValueFactory(0, 23));
        beginMinuteField.setValueFactory(new LoopingIntegerSpinnerValueFactory(0, 59));
        endHourField.setValueFactory(new LoopingIntegerSpinnerValueFactory(0, 23));
        endMinuteField.setValueFactory(new LoopingIntegerSpinnerValueFactory(0, 59));
        amountHourField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99));
        amountMinuteField.setValueFactory(new LoopingIntegerSpinnerValueFactory(0, 59));

        // places choicebox
        placeChoiceBox.setEditable(true);
        placeChoiceBox.setItems(model.getProfile().getPlaces());

        btOk = (Button) lookupButton(ButtonType.OK);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateAndStore()) {
                event.consume();
            }
        });

    }

    private void initFields() {
        // date fields
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        beginDatePicker.setValue(yesterday);
        endDatePicker.setValue(today);

    }

    private void addFormBindings() {
        // Custom amount binding
        amountHourField.disableProperty().bind(amountCheckBox.selectedProperty().not());
        amountMinuteField.disableProperty().bind(amountCheckBox.selectedProperty().not());


        // custom amount
        amountCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                amountHourField.getEditor().clear();
                amountMinuteField.getEditor().clear();
            }
        });
    }

    private boolean validateAndStore() {
        if (beginDatePicker.getValue() == null
                || endDatePicker.getValue() == null
                || beginHourField.getValue() == null
                || beginMinuteField.getValue() == null
                || endHourField.getValue() == null
                || endMinuteField.getValue() == null)
            return false;

        LocalDateTime beginDT = LocalDateTime.of(beginDatePicker.getValue().getYear(),
                beginDatePicker.getValue().getMonth(), beginDatePicker.getValue().getDayOfMonth(),
                Integer.parseInt(beginHourField.getEditor().getText()),
                Integer.parseInt(beginMinuteField.getEditor().getText()));
        LocalDateTime endDT = LocalDateTime.of(endDatePicker.getValue().getYear(),
                endDatePicker.getValue().getMonth(), endDatePicker.getValue().getDayOfMonth(),
                Integer.parseInt(endHourField.getEditor().getText()),
                Integer.parseInt(endMinuteField.getEditor().getText()));

        if (amountCheckBox.isSelected() && amountHourField.getValue() == null && amountMinuteField.getValue() == null)
            return false;
        Duration amount = null;
        String hstr = amountHourField.getEditor().getText();
        String mstr = amountMinuteField.getEditor().getText();
        if (hstr != null && !hstr.isEmpty() && mstr != null && !mstr.isEmpty())
            amount = Duration.ofHours(Long.parseLong(hstr)).plusMinutes(Long.parseLong(mstr));
        else if (hstr != null && !hstr.isEmpty())
            amount = Duration.ofHours(Long.parseLong(hstr));
        else if (mstr != null && !mstr.isEmpty())
            amount = Duration.ofMinutes(Long.parseLong(mstr));

        if (endDT.isAfter(LocalDateTime.now())
                || beginDT.isAfter(endDT)
                || beginDT.isEqual(endDT)
                || placeChoiceBox.getValue() == null || placeChoiceBox.getValue().isEmpty()
                || amountCheckBox.isSelected() && (amount == null || amount.compareTo(Duration.between(beginDT, endDT)) > 0))
            return false;

        // else if valid :
        sleepItem.setBegin(beginDT);
        sleepItem.setEnd(endDT);
        if (amountCheckBox.isSelected()) {
            sleepItem.setAmount(amount);
        }
        sleepItem.setAlone(aloneCheckBox.isSelected());
        sleepItem.setWhere(placeChoiceBox.getValue());
        return true;
    }

    public SleepItem getSleepItem() {
        return validateAndStore() ? sleepItem : null;
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
