package controller;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import model.SleepItem;
import model.SleepModel;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
    private ChoiceBox placeChoiceBox;
    @FXML
    private CheckBox amountCheckBox;
    @FXML
    private Spinner amountHourField;
    @FXML
    private Spinner amountMinuteField;
    @FXML
    private Button addItemButton;

    private SleepModel model = SleepModel.getInstance();
    final private SleepItem editingSleepItem = new SleepItem();

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
        initFields();
        addFormBindings();
    }

    private void initFields() {
        // time fields
        beginHourField.setValueFactory(new FormController.LoopingIntegerSpinnerValueFactory(0, 23, 23));
        beginMinuteField.setValueFactory(new FormController.LoopingIntegerSpinnerValueFactory(0, 59, 0));
        endHourField.setValueFactory(new FormController.LoopingIntegerSpinnerValueFactory(0, 23, 8));
        endMinuteField.setValueFactory(new FormController.LoopingIntegerSpinnerValueFactory(0, 59, 30));
        amountHourField.setValueFactory(new LoopingIntegerSpinnerValueFactory(0, 100));
        amountMinuteField.setValueFactory(new FormController.LoopingIntegerSpinnerValueFactory(0, 59));
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
