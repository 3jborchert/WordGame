package Screens;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class ScreenOne {

    private StackPane root;
    private Button button;
    private ScreenManager screenManager;

    public ScreenOne(ScreenManager screenManager) {
        this.screenManager = screenManager;
        root = new StackPane();
        button = new Button("Go to Screen Two");

        button.setOnAction(e -> screenManager.showScreen("ScreenTwo"));

        root.getChildren().add(button);
    }

    public StackPane getRoot() {
        return root;
    }
}

