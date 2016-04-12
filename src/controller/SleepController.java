package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.SleepItem;
import model.SleepModel;
import model.SleepProfile;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

public class SleepController implements Observer, Initializable{
    @FXML
    private BorderPane rootBorderPane;
    @FXML
    private ListView sleepItemsListView;

    private SleepModel model = SleepModel.getInstance();
    private File currentFile;

    private Stage getStage() {
        return (Stage) rootBorderPane.getScene().getWindow();
    }

    public SleepController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sleepItemsListView.itemsProperty().addListener((observable, oldValue, newValue) -> {
            sleepItemsListView.scrollTo(sleepItemsListView.getItems().size()-1);
        });

        // register to the model
        model.addObserver(this);
    }

    @FXML
    public void loadProfile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open profile");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File f = fileChooser.showOpenDialog(getStage());

        if (f != null) {
            model.loadProfile(f);
            currentFile = f;
        }
    }

    @FXML
    public void saveProfile() {
        if (currentFile != null) {
            model.saveProfile(currentFile);
        } else {
            this.saveProfileAs();
        }
    }

    @FXML
    public void saveProfileAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save profile");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File f = fileChooser.showSaveDialog(getStage());

        if (f != null) {
            model.saveProfile(f);
            currentFile = f;
        }
    }

    @FXML
    public void addSleepItem() {
        SleepItemDialog d = new SleepItemDialog();
        Optional<SleepItem> toAdd = d.showAndWait();

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
        Platform.exit();
    }

    @Override
    public void update(Observable o, Object arg) {
        SleepProfile sp = (SleepProfile) arg;
        System.out.println("profile changed notification : " + sp.getName());

        getStage().setTitle("Sleep - " + sp.getName());
        sleepItemsListView.setItems(sp.getSleepItems());
    }
}
