package FontSchemes;

public class FontScheme {
    public static final FontScheme DEFAULT = new FontScheme("Arial", 14);
    public static final FontScheme LARGE = new FontScheme("Arial", 18);
    public static final FontScheme SMALL = new FontScheme("Arial", 10);

    private String fontFamily;
    private int fontSize;

    public FontScheme(String fontFamily, int fontSize) {
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public int getFontSize() {
        return fontSize;
    }

    // Override toString to return a more user-friendly string
    @Override
    public String toString() {
        if (this == DEFAULT) {
            return "Default";
        } else if (this == LARGE) {
            return "Large";
        } else if (this == SMALL) {
            return "Small";
        }
        return super.toString();  // Default behavior if none of the above matches
    }
}

