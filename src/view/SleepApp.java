package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SleepApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
        primaryStage.setTitle("Sleep");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(900);
        primaryStage.setResizable(true);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
