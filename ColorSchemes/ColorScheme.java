package ColorSchemes;

import javafx.scene.paint.Color;

public class ColorScheme {
    // Predefined color schemes
    public static final ColorScheme DEFAULT = new ColorScheme(Color.LIGHTGRAY, Color.BLACK);
    public static final ColorScheme DARK = new ColorScheme(Color.BLACK, Color.WHITE);
    public static final ColorScheme LIGHT = new ColorScheme(Color.WHITE, Color.BLACK);

    private Color backgroundColor;
    private Color textColor;

    public ColorScheme(Color backgroundColor, Color textColor) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    // Helper methods to return color as hex
    public String getBackgroundColorHex() {
        return toHex(backgroundColor);
    }

    // Define the getDarkColorScheme() method that was referenced
    public static ColorScheme getDefaultColorScheme() {
        return DEFAULT;  // Return the predefined DARK color scheme
    }

    // Define the getDarkColorScheme() method that was referenced
    public static ColorScheme getDarkColorScheme() {
        return DARK;  // Return the predefined DARK color scheme
    }

    // Define the getDarkColorScheme() method that was referenced
    public static ColorScheme getLightColorScheme() {
        return LIGHT;  // Return the predefined DARK color scheme
    }

    public String getTextColorHex() {
        return toHex(textColor);
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X", (int)(color.getRed() * 255),
                                     (int)(color.getGreen() * 255), 
                                     (int)(color.getBlue() * 255));
    }

    // Override toString to return a more user-friendly string
    @Override
    public String toString() {
        if (this == DEFAULT) {
            return "Default";
        } else if (this == DARK) {
            return "Dark";
        } else if (this == LIGHT) {
            return "Light";
        }
        return super.toString();  // Default behavior if none of the above matches
    }
}

