package Screens;

import ColorSchemes.*;
import FontSchemes.*;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ColorSchemes.ColorScheme;
import FontSchemes.FontScheme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class SettingsScreen {

    private VBox root;
    private ScreenManager screenManager;
    private ComboBox<String> colorSchemeCombo;
    private ComboBox<String> fontSchemeCombo;
    private Button applyButton;

    public SettingsScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        root = new VBox(20);

        colorSchemeCombo = new ComboBox<>();
        colorSchemeCombo.getItems().addAll("Default", "Dark", "Light"); // Use strings for color schemes
        colorSchemeCombo.setValue("Default");  

        fontSchemeCombo = new ComboBox<>();
        fontSchemeCombo.getItems().addAll("Default", "Large", "Small");  // Use strings for font schemes
        fontSchemeCombo.setValue("Default");  

        colorSchemeCombo.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: black;");
        fontSchemeCombo.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: black;");

        applyButton = new Button("Apply Settings");
        applyButton.setOnAction(e -> {
            String selectedColorScheme = colorSchemeCombo.getValue();
            String selectedFontScheme = fontSchemeCombo.getValue();

            ColorScheme colorScheme = getColorScheme(selectedColorScheme);
            FontScheme fontScheme = getFontScheme(selectedFontScheme);

            screenManager.applySettings(colorScheme, fontScheme);

            screenManager.showScreen("HomeScreen");
        });

        applyButton.setStyle("-fx-background-color: grey; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2px;");
        applyButton.setMinSize(120, 40);

        root.getChildren().addAll(colorSchemeCombo, fontSchemeCombo, applyButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
    }

    public VBox getRoot() {
        return root;
    }

    // Method to map selected string to ColorScheme
    private ColorScheme getColorScheme(String selectedScheme) {
        switch (selectedScheme) {
            case "Dark":
                return ColorScheme.DARK;
            case "Light":
                return ColorScheme.LIGHT;
            case "Default":
            default:
                return ColorScheme.DEFAULT;
        }
    }

    // Method to map selected string to FontScheme
    private FontScheme getFontScheme(String selectedScheme) {
        switch (selectedScheme) {
            case "Large":
                return FontScheme.LARGE;
            case "Small":
                return FontScheme.SMALL;
            case "Default":
            default:
                return FontScheme.DEFAULT;
        }
    }
}

