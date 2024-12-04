package Screens;

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
    private Button playAgainButton;
    private Button homeButton; // Home button
    private List<List<TextField>> currentGuessBoxes;
    private int currentAttempt;
    private List<String> possibleWords;
    private String correctWord;
    private int wordLength;
    private boolean gameWon;

    public StanderedGameScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        root = new VBox(20);
        grid = new GridPane();
        submitButton = new Button("Submit");
        playAgainButton = new Button("Play Again");
        homeButton = new Button("Home"); // Initialize Home button
        currentGuessBoxes = new ArrayList<>();
        currentAttempt = 0; // Start from attempt 0
        gameWon = false;

        loadPossibleWords();
        initializeGame();

        submitButton.setOnAction(e -> handleSubmitGuess());
        playAgainButton.setOnAction(e -> restartGame());
        homeButton.setOnAction(e -> goToHomeScreen()); // Home button action

        // Layout adjustments
        HBox topBar = new HBox(10, homeButton); // Add home button to the top bar
        topBar.setAlignment(Pos.TOP_LEFT);

        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(topBar, grid, submitButton, playAgainButton);
        playAgainButton.setVisible(false); // Hide play again button initially
        homeButton.setVisible(true); // Make sure the Home button is visible

        // Center the game grid
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
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
        gameWon = false;

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
    }

    private TextField createGuessBox() {
        TextField textField = new TextField();
        textField.setMaxWidth(50);
        textField.setMaxHeight(50);
        textField.setAlignment(Pos.CENTER);
        textField.setFont(Font.font(24)); // Increased font size
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
        // Prevent submitting after the game is won
        if (gameWon)
            return;

        // Get the current guess from the text fields
        StringBuilder guess = new StringBuilder();
        for (TextField box : currentGuessBoxes.get(currentAttempt)) {
            guess.append(box.getText().toLowerCase());
        }

        // Check if the guess is valid (same length as the correct word)
        if (guess.length() != wordLength) {
            System.out.println("Invalid guess length.");
            return;
        }

        // Check the guess against the correct word
        String guessStr = guess.toString();
        if (guessStr.equals(correctWord)) {
            updateGuessBoxes(true);
            gameWon = true;
            System.out.println("You win!");
            playAgainButton.setVisible(true); // Show play again button
        } else {
            updateGuessBoxes(false);
            currentAttempt++; // Increment the attempt after a guess

            // Check if we have reached max attempts
            if (currentAttempt >= 6) {
                System.out.println("Game Over. The correct word was: " + correctWord);
                playAgainButton.setVisible(true); // Show play again button
            }
        }
    }

    private void updateGuessBoxes(boolean isCorrect) {
        List<TextField> row = currentGuessBoxes.get(currentAttempt); // Adjusted for 0-based index
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
            addSymbolToBox(box, "Check");
        } else if (correctWord.contains(String.valueOf(guessedChar))) {
            box.setStyle("-fx-background-color: orange; -fx-border-color: black;");
            addSymbolToBox(box, "Dash");
        } else {
            box.setStyle("-fx-background-color: red; -fx-border-color: black;");
            addSymbolToBox(box, "X");
        }
        box.setEditable(false); // Lock the box after submit
    }

    private void addSymbolToBox(TextField box, String symbol) {
        // Create a StackPane to hold the TextField and symbol
        StackPane pane = new StackPane();
        pane.setAlignment(Pos.TOP_RIGHT); // Align the symbol to the top-right corner

        // Create the symbol (image) to add to the box
        ImageView symbolImage = null;
        try {
            // Adjust path to point to src/Resources/Symbols/
            String imagePath = "Resources/Symbols/" + symbol + ".png"; // Relative to the src dir
            File imageFile = new File(imagePath);

            if (imageFile.exists()) {
                // Load image using the file system path
                Image image = new Image(imageFile.toURI().toString());
                symbolImage = new ImageView(image);
                symbolImage.setFitWidth(25); // Larger image size
                symbolImage.setFitHeight(25);
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

    private void restartGame() {
        loadPossibleWords();
        initializeGame();
        playAgainButton.setVisible(false); // Hide play again button after restart
    }

    private void goToHomeScreen() {
        screenManager.showScreen("HomeScreen"); // Assuming you have a method for showing the home screen
    }

    public VBox getRoot() {
        return root;
    }
}
