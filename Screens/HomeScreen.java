package Screens;

import javafx.geometry.Pos;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

public class HomeScreen {

    private VBox root;
    private Button playButton;
    private Button settingsButton;
    private Label gameTitle;
    private ScreenManager screenManager;

    public HomeScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        root = new VBox(20);
        playButton = new Button("Play");
        settingsButton = new Button("Settings");
        gameTitle = new Label("Word Game");

        playButton.setOnAction(e -> screenManager.showScreen("StanderedGameScreen"));
        settingsButton.setOnAction(e -> screenManager.showScreen("SettingsScreen"));

        root.getChildren().addAll(gameTitle,playButton,settingsButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
    }

    public VBox getRoot() {
        return root;
    }

}
