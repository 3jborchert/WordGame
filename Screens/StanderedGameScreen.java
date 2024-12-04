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
        currentAttempt = 0; // Start from attempt 0

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
        // Print the correct word to the terminal for debugging
        System.out.println("Correct word: " + correctWord);

        // Get the current guess from the text fields
        StringBuilder guess = new StringBuilder();
        for (TextField box : currentGuessBoxes.get(currentAttempt)) {
            guess.append(box.getText().toLowerCase());
        }

        // Print the current guess to the terminal
        System.out.println("Current guess: " + guess);

        // Check if the guess is valid (same length as the correct word)
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
            currentAttempt++; // Increment the attempt after a guess

            // Check if we have reached max attempts
            if (currentAttempt >= 6) {
                System.out.println("Game Over. The correct word was: " + correctWord);
            } else {
                currentGuessBoxes.add(new ArrayList<>()); // Prepare for the next row of guesses
            }
        }
    }

    private void updateGuessBoxes(boolean isCorrect) {
        // Ensure we're working with a valid attempt index
        if (currentAttempt <= 0) {
            System.out.println("Invalid attempt index.");
            return;
        }

        List<TextField> row = currentGuessBoxes.get(currentAttempt); // currentAttempt -1 Adjust index to match attempts
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

        // Create the symbol (image) to add to the box
        ImageView symbolImage = null;
        try {
            // Adjust path for resources in the src/Resources folder
            String imagePath = "Resources/Symbols/" + symbol + ".png"; // Corrected path
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                // Create an Image from the InputStream
                Image image = new Image(imageStream);
                symbolImage = new ImageView(image);
                symbolImage.setFitWidth(15);
                symbolImage.setFitHeight(15);
            } else {
                System.out.println("Image not found: " + imagePath);
            }
        } catch (Exception e) {
            System.out.println("Error loading symbol: " + e.getMessage());
        }

        // Add the symbol to the pane
        if (symbolImage != null) {
            pane.getChildren().add(symbolImage);
        }

        // Create a new StackPane that wraps the TextField and the symbol
        StackPane textFieldWithSymbol = new StackPane();
        textFieldWithSymbol.getChildren().addAll(box, pane);

        // Replace the original TextField with the new StackPane
        grid.add(textFieldWithSymbol, GridPane.getColumnIndex(box), GridPane.getRowIndex(box));
    }

    public VBox getRoot() {
        return root;
    }
}
