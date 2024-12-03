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

public class SettingsScreen {

    private VBox root;
    private ScreenManager screenManager;
    private ComboBox<String> colorSchemeCombo;
    private ComboBox<String> fontSchemeCombo;
    private Button applyButton;

    public SettingsScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        root = new VBox(20);

        // Create ComboBox for color scheme
        colorSchemeCombo = new ComboBox<>();
        colorSchemeCombo.getItems().addAll("Default", "Dark", "Light"); // Use strings for color schemes
        colorSchemeCombo.setValue("Default");  // Default selection

        // Create ComboBox for font scheme
        fontSchemeCombo = new ComboBox<>();
        fontSchemeCombo.getItems().addAll("Default", "Large", "Small");  // Use strings for font schemes
        fontSchemeCombo.setValue("Default");  // Default selection

        // Create Apply button to apply selected settings
        applyButton = new Button("Apply Settings");
        applyButton.setOnAction(e -> {
            // Get selected color and font schemes as strings
            String selectedColorScheme = colorSchemeCombo.getValue();
            String selectedFontScheme = fontSchemeCombo.getValue();

            // Convert selected strings to ColorScheme and FontScheme objects
            ColorScheme colorScheme = getColorScheme(selectedColorScheme);
            FontScheme fontScheme = getFontScheme(selectedFontScheme);

            // Apply the settings via ScreenManager
            screenManager.applySettings(colorScheme, fontScheme);

            // Optionally, navigate back to the home screen or another screen
            screenManager.showScreen("HomeScreen");
        });

        // Add all components to root
        root.getChildren().addAll(colorSchemeCombo, fontSchemeCombo, applyButton);
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

