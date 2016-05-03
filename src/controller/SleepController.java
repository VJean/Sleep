package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.SleepItem;
import model.SleepModel;
import model.SleepProfile;

import java.io.File;
import java.net.URL;
import java.util.*;

public class SleepController implements Observer, Initializable{
    @FXML
    private VBox rootContainer;
    @FXML
    private ListView<SleepItem> sleepItemsListView;
    @FXML
    private GridPane detailsTable; // TODO Grid layout instead
    @FXML
    private ToggleButton detailsButton;
    @FXML
    private Label detailsBeginLabel;
    @FXML
    private Label detailsEndLabel;
    @FXML
    private Label detailsDurationLabel;
    @FXML
    private Label detailsAloneLabel;
    @FXML
    private Label detailsPlaceLabel;
    @FXML
    private Label logLabel;
    @FXML
    private Accordion chartContainer;

    private PieChart placesChart = new PieChart();
    //private LineChart<String, Number> amountLineChart;

    private SleepModel model = SleepModel.getInstance();
    private File currentFile;

    private Stage getStage() {
        return (Stage) rootContainer.getScene().getWindow();
    }

    private void updateDetails(Map<String, String> details) {
        detailsBeginLabel.setText(details.get("Begin"));
        detailsEndLabel.setText(details.get("End"));
        detailsDurationLabel.setText(details.get("Duration"));
        detailsAloneLabel.setText(details.get("Alone"));
        detailsPlaceLabel.setText(details.get("Place"));
    }

    public SleepController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //detailsTable.visibleProperty().bind(detailsButton.selectedProperty());

        sleepItemsListView.itemsProperty().addListener((observable, oldValue, newValue) -> {
            sleepItemsListView.scrollTo(sleepItemsListView.getItems().size()-1);
            // TODO set tooltips ?
        });
        sleepItemsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateDetails(newValue.detailsToMap());
        });

        placesChart.setLegendVisible(false);
        //chartContainer.getChildren().add(placesChart);

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

        // places
        Map<String, Double> placesCountMap = new HashMap<>();
        sp.getPlaces().forEach(p -> placesCountMap.put(p, 0d));
        for(SleepItem i: sp.getSleepItems()){
            placesCountMap.put(i.getWhere(), placesCountMap.get(i.getWhere()) + 1);
        }
        List<PieChart.Data> dl = new ArrayList<>();
        placesCountMap.entrySet().forEach(e -> dl.add(new PieChart.Data(e.getKey(), e.getValue())));
        ObservableList<PieChart.Data> d = FXCollections.observableList(dl);
        placesChart.setData(d);

        // amount
//        XYChart.Series series = new XYChart.Series();
//        for (SleepItem i: sp.getSleepItems().sorted()) {
//            series.getData().add(new XYChart.Data<String, Number>(i.getEnd().toLocalDate().toString(), i.getAmount().getSeconds()));
//        }
//        amountLineChart.getData().add(series);

    }
}
