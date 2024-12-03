package Screens;

import FontSchemes.*;
import ColorSchemes.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.HashMap;
import java.util.Map;

public class ScreenManager {

    private Stage stage;
    private Map<String, Scene> screens;
    private ColorScheme currentColorScheme;
    private FontScheme currentFontScheme;

    public ScreenManager(Stage stage) {
        this.stage = stage;
        screens = new HashMap<>();
        currentColorScheme = ColorScheme.DEFAULT;  // Use predefined DEFAULT scheme
        currentFontScheme = FontScheme.DEFAULT;    // Use predefined DEFAULT font scheme

        // Initialize all screens and store them in the map
        try {
            screens.put("HomeScreen", createHomeScreen());
            screens.put("SettingsScreen", createSettingsScreen());
            screens.put("StanderedGameScreen", createStanderedGameScreen());
            screens.put("ScreenOne", createScreenOne());
            screens.put("ScreenTwo", createScreenTwo());
        } catch (Exception e) {
            System.out.println("Error initializing screens: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to show a screen based on the screen name
    public void showScreen(String screenName) {
        Scene scene = screens.get(screenName);
        if (scene == null) {
            throw new IllegalArgumentException("Screen '" + screenName + "' not found.");
        }
        stage.setScene(scene);
    }

    // Apply settings for ColorScheme and FontScheme
    public void applySettings(ColorScheme colorScheme, FontScheme fontScheme) {
        // Apply color scheme
        applyColorScheme(colorScheme);

        // Apply font scheme
        applyFontScheme(fontScheme);
    }

    private void applyColorScheme(ColorScheme colorScheme) {
        // Apply the color scheme to all screens
        for (Scene scene : screens.values()) {
            scene.getRoot().setStyle("-fx-background-color: " + colorScheme.getBackgroundColorHex());

            // Optionally, change text color for all labels and buttons
            scene.getRoot().getChildrenUnmodifiable().forEach(node -> {
                if (node instanceof Button || node instanceof Label) {
                    node.setStyle("-fx-text-fill: " + colorScheme.getTextColorHex());
                }
            });
        }
    }

    private void applyFontScheme(FontScheme fontScheme) {
        // Apply the font scheme to all screens
        for (Scene scene : screens.values()) {
            scene.getRoot().getChildrenUnmodifiable().forEach(node -> {
                if (node instanceof Button || node instanceof Label) {
                    node.setStyle("-fx-font-family: '" + fontScheme.getFontFamily() + "';" +
                            "-fx-font-size: " + fontScheme.getFontSize() + "px;");
                }
            });
        }
    }

    private Scene createScreenOne() {
        ScreenOne screenOne = new ScreenOne(this);
        return new Scene(screenOne.getRoot(), 400, 300);
    }

    private Scene createScreenTwo() {
        ScreenTwo screenTwo = new ScreenTwo(this);
        return new Scene(screenTwo.getRoot(), 400, 300);
    }

    private Scene createHomeScreen() {
        HomeScreen homeScreen = new HomeScreen(this);
        return new Scene(homeScreen.getRoot(), 1001, 800);
    }

    private Scene createSettingsScreen() {
        SettingsScreen settingsScreen = new SettingsScreen(this);
        return new Scene(settingsScreen.getRoot(), 1000, 800);
    }

    private Scene createStanderedGameScreen() {
        StanderedGameScreen standeredGameScreen = new StanderedGameScreen(this);
        return new Scene(standeredGameScreen.getRoot(), 1000, 800);
    }

    // Convert a Color to Hex representation
    private String toHex(Color color) {
        return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}

