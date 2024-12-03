package Screens;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class StanderedGameScreen {
    private VBox root;
    private ScreenManager screenManager;
    private GridPane grid;
    private Button submitButton;
    private List<List<TextField>> currentGuessBoxes;
    private int currentAttempt;
    private List<String> possibleWords;
    private String correctWord;
    private int wordLength;

    public StanderedGameScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        root = new VBox(20);
        grid = new GridPane();
        submitButton = new Button("Submit");
        currentGuessBoxes = new ArrayList<>();
        currentAttempt = 0;

        loadPossibleWords();
        initializeGame();

        submitButton.setOnAction(e -> handleSubmitGuess());

        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(grid, submitButton);
    }

    private void loadPossibleWords() {
        // Load words from the resources folder (in this case, we assume it contains a
        // list of words)
        try (BufferedReader reader = new BufferedReader(new FileReader("Resources/RandomStanderedGameWords.txt"))) {
            possibleWords = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                possibleWords.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Error loading words: " + e.getMessage());
        }

        // Randomly select a word from the list
        correctWord = possibleWords.get(new Random().nextInt(possibleWords.size()));
        wordLength = correctWord.length();
    }

    private void initializeGame() {
        grid.getChildren().clear();
        currentGuessBoxes.clear();
        currentAttempt = 0;

        // Create input boxes based on the word length
        for (int row = 0; row < 6; row++) {
            List<TextField> rowBoxes = new ArrayList<>();
            for (int col = 0; col < wordLength; col++) {
                TextField box = createGuessBox();
                grid.add(box, col, row);
                rowBoxes.add(box);
            }
            currentGuessBoxes.add(rowBoxes);
        }

        // Arrange grid layout
        grid.setVgap(10);
        grid.setHgap(10);
    }

    private TextField createGuessBox() {
        TextField textField = new TextField();
        textField.setMaxWidth(40);
        textField.setMaxHeight(40);
        textField.setAlignment(Pos.CENTER);
        textField.setFont(Font.font(20));
        textField.setStyle("-fx-background-color: white; -fx-border-color: black;");

        textField.textProperty().addListener((obs, oldText, newText) -> {
            // Ensure only a single character is entered
            if (newText.length() > 1) {
                textField.setText(newText.substring(0, 1));
            }
        });

        return textField;
    }

    private void handleSubmitGuess() {
        // Check if we have reached max attempts first
        if (currentAttempt >= 6) {
            System.out.println("Game Over. The correct word was: " + correctWord);
            return;
        }

        // Get the current guess from the text fields in the current attempt row
        StringBuilder guess = new StringBuilder();
        List<TextField> guessRow = currentGuessBoxes.get(currentAttempt);
        for (TextField box : guessRow) {
            guess.append(box.getText().toLowerCase());
        }

        // Ensure the guess length is valid
        if (guess.length() != wordLength) {
            System.out.println("Invalid guess length.");
            return;
        }

        // Check the guess against the correct word
        String guessStr = guess.toString();
        if (guessStr.equals(correctWord)) {
            updateGuessBoxes(true);
            System.out.println("You win!");
        } else {
            updateGuessBoxes(false);
            currentAttempt++;

            // Add a new row if there are more attempts left
            if (currentAttempt < 6) {
                List<TextField> nextRow = new ArrayList<>();
                for (int i = 0; i < wordLength; i++) {
                    nextRow.add(createGuessBox());
                }
                currentGuessBoxes.add(nextRow);
                updateGridLayout();
            }
        }
    }

    private void updateGuessBoxes(boolean isCorrect) {
        // Only update the current row for the active attempt
        List<TextField> row = currentGuessBoxes.get(currentAttempt - 1); // Current row for the current attempt
        for (int i = 0; i < row.size(); i++) {
            TextField box = row.get(i);
            char guessedChar = box.getText().charAt(0);
            char correctChar = correctWord.charAt(i);
            updateBoxAppearance(box, guessedChar, correctChar);
        }
    }

    private void updateBoxAppearance(TextField box, char guessedChar, char correctChar) {
        if (guessedChar == correctChar) {
            box.setStyle("-fx-background-color: green; -fx-border-color: black;");
            addSymbolToBox(box, "check");
        } else if (correctWord.contains(String.valueOf(guessedChar))) {
            box.setStyle("-fx-background-color: yellow; -fx-border-color: black;");
            addSymbolToBox(box, "dash");
        } else {
            box.setStyle("-fx-background-color: red; -fx-border-color: black;");
            addSymbolToBox(box, "x");
        }
        box.setEditable(false); // Lock the box after submit
    }

    private void addSymbolToBox(TextField box, String symbol) {
        // Create a StackPane to hold the TextField and symbol
        StackPane pane = new StackPane();
        pane.setAlignment(Pos.TOP_RIGHT); // Align the symbol to the top right corner

        // Corrected path: Resources is not included in the path (it’s in the classpath)
        String imagePath = "/Symbols/" + symbol + ".png";
        ImageView symbolImage = null;

        // Try loading the symbol image
        try {
            // Create an Image from the InputStream
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                Image image = new Image(imageStream);
                symbolImage = new ImageView(image);
                symbolImage.setFitWidth(15);
                symbolImage.setFitHeight(15);
                pane.getChildren().add(symbolImage); // Add the image to the pane
            } else {
                System.out.println("Image not found: " + imagePath);
            }
        } catch (Exception e) {
            System.out.println("Error loading image for symbol: " + symbol + " (" + imagePath + ")");
            e.printStackTrace();
        }

        // Create a new StackPane that wraps the TextField and the symbol
        StackPane textFieldWithSymbol = new StackPane();
        textFieldWithSymbol.getChildren().addAll(box, pane);

        // Replace the original TextField with the new StackPane
        grid.add(textFieldWithSymbol, GridPane.getColumnIndex(box), GridPane.getRowIndex(box));
    }

    private void updateGridLayout() {
        grid.getChildren().clear();
        for (int row = 0; row < currentGuessBoxes.size(); row++) {
            List<TextField> guessRow = currentGuessBoxes.get(row);
            for (int col = 0; col < guessRow.size(); col++) {
                TextField box = guessRow.get(col);
                grid.add(box, col, row);
            }
        }
    }

    public VBox getRoot() {
        return root;
    }
}
