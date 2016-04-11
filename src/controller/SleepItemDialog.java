package controller;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import model.SleepItem;

/**
 * Created by JeanV on 11/04/2016.
 */
public class SleepItemDialog extends Dialog<SleepItem> {

    final private FormController form;

    public SleepItemDialog() {
        super();

        form = new FormController();
        setDialogPane(form);
        setResultConverter(param ->
                param == ButtonType.OK
                        ? form.getSleepItem()
                        : null);
    }
}
