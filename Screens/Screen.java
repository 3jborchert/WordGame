package Screens;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public abstract class Screen {
    protected Scene scene;

    public Screen() {
        StackPane root = new StackPane();
        root.getChildren().add(new Text("This is a screen!"));
        scene = new Scene(root, 800, 600);
    }

    public Scene getScene() {
        return scene;
    }

    public abstract void initialize();
}

