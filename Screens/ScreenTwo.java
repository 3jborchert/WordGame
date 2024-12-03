package Screens;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class ScreenTwo {

    private StackPane root;
    private Button button;
    private ScreenManager screenManager;

    public ScreenTwo(ScreenManager screenManager) {
        this.screenManager = screenManager;
        root = new StackPane();
        button = new Button("Go to Screen One");

        button.setOnAction(e -> screenManager.showScreen("ScreenOne"));

        root.getChildren().add(button);
    }

    public StackPane getRoot() {
        return root;
    }
}

