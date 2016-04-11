package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.SleepItem;
import model.SleepModel;
import model.SleepProfile;

import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class SleepController implements Observer {
    @FXML
    private BorderPane rootBorderPane;
    @FXML
    private ListView sleepItemsListView;

    private SleepModel model = SleepModel.getInstance();

    private Stage getStage() {
        return (Stage) rootBorderPane.getScene().getWindow();
    }

    public SleepController() {
    }

    public void initialize() {
        //addFormBindings();

        // register to the model
        model.addObserver(this);
    }

    @FXML
    public void loadProfile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open profile");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File f = fileChooser.showOpenDialog(rootBorderPane.getScene().getWindow());

        if (f != null) {
            model.loadProfile(f);
        }
    }

    @FXML
    public void saveProfile() {
        model.saveProfile();
    }

    @FXML
    public void addSleepItem() {
        Optional<SleepItem> toAdd;

        SleepItemDialog d = new SleepItemDialog();

        toAdd = d.showAndWait();

        Alert alert = new Alert(Alert.AlertType.NONE);

        if (toAdd.isPresent() && this.model.addItem(toAdd.get())) {
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
        sleepItemsListView.setItems(sp.getSleepItems());
    }
}
