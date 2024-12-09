//Justin Borchert
//CSC 1061
//10/25/2024

import Screens.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;

public class Launch extends Application {

    private ScreenManager screenManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        screenManager = new ScreenManager(primaryStage);

        screenManager.showScreen("HomeScreen");

        primaryStage.setTitle("Word Game");
        primaryStage.show();
    }
}
