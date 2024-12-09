package Screens;

import PlayerStuff.PlayerInterface;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.*;
import java.util.*;

public class StanderedGameScreen {

    private VBox root;
    private ScreenManager screenManager;
    private GridPane grid;
    private Button submitButton;
    private Button homeButton;
    private Button replayButton;
    private List<List<TextField>> currentGuessBoxes;
    private int currentAttempt;
    private List<String> possibleWords;
    private String correctWord;
    private int wordLength;
    private Text gameOverText;

    private Text winsText;
    private Text lossesText;
    private Text avgGuessesText;
    private Text mostGuessedWordText;
    private Text mostGuessedLetterText;

    public StanderedGameScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        root = new VBox(20);
        grid = new GridPane();
        submitButton = new Button("Submit");
        homeButton = new Button("Home");
        replayButton = new Button("Play Again");
        currentGuessBoxes = new ArrayList<>();
        currentAttempt = 0;

        gameOverText = new Text();
        gameOverText.setFont(Font.font(18));
        gameOverText.setFill(Color.RED);

        winsText = new Text("Wins: 0");
        lossesText = new Text("Losses: 0");
        avgGuessesText = new Text("Avg Guesses: 0");
        mostGuessedWordText = new Text("Most Guessed Word: N/A");
        mostGuessedLetterText = new Text("Most Guessed Letter: N/A");

        loadPossibleWords();
        initializeGame();

        // Set up the Home button action (top-left corner)
        homeButton.setOnAction(e -> goHome());
        homeButton.setStyle(
                "-fx-font-size: 14px; -fx-background-color: grey; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2px;");
        homeButton.setMinSize(100, 40);

        replayButton.setOnAction(e -> startNewGame());
        replayButton.setVisible(false);
        replayButton.setStyle(
                "-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 10px;");
        replayButton.setMinSize(120, 40);

        // Set up Submit button action
        submitButton.setOnAction(e -> handleSubmitGuess());
        submitButton.setStyle(
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 10px;");
        submitButton.setMinSize(120, 40); // Size the button properly

        root.setAlignment(Pos.CENTER);

        HBox topLayout = new HBox(20, homeButton, winsText, lossesText, avgGuessesText);
        topLayout.setAlignment(Pos.TOP_LEFT);

        root.getChildren().addAll(topLayout, gameOverText, grid, submitButton, replayButton);

        HBox bottomLayout = new HBox(20, mostGuessedWordText, mostGuessedLetterText);
        bottomLayout.setAlignment(Pos.BOTTOM_LEFT);
        root.getChildren().add(bottomLayout);
    }

    private void loadPossibleWords() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Resources/RandomStanderedGameWords.txt"))) {
            possibleWords = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                possibleWords.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Error loading words: " + e.getMessage());
        }

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

        // Arrange grid layout: Center the grid in the VBox
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        updateStats();
    }

    private TextField createGuessBox() {
        TextField textField = new TextField();
        textField.setMaxWidth(70);
        textField.setMaxHeight(80);
        textField.setAlignment(Pos.CENTER);
        textField.setFont(Font.font(30));
        textField.setStyle("-fx-background-color: white; -fx-border-color: black;");

        // Add listener to ensure only one letter is entered and auto-move to the next
        // box
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() > 1) {
                textField.setText(newText.substring(0, 1));
            }
            // Automatically move to the next box if a key is entered
            if (newText.length() == 1) {
                moveToNextBox(textField);
            }
        });

        return textField;
    }

    private void moveToNextBox(TextField currentBox) {
        // Get the position of the current box
        int rowIndex = GridPane.getRowIndex(currentBox);
        int colIndex = GridPane.getColumnIndex(currentBox);

        // Move to the next box in the row, if available
        if (colIndex < wordLength - 1) {
            TextField nextBox = currentGuessBoxes.get(rowIndex).get(colIndex + 1);
            nextBox.requestFocus();
        }
    }

    private void handleSubmitGuess() {
        // Get the current guess from the text fields
        StringBuilder guess = new StringBuilder();
        for (TextField box : currentGuessBoxes.get(currentAttempt)) {
            guess.append(box.getText().toLowerCase());
        }

        if (guess.length() != wordLength) {
            System.out.println("Invalid guess length.");
            return;
        }

        // Add guessed word to list
        PlayerInterface.saveGuessedWord(guess.toString());

        // Check the guess against the correct word
        String guessStr = guess.toString();
        if (guessStr.equals(correctWord)) {
            updateGuessBoxes(true);
            System.out.println("You win!");
            PlayerInterface.saveWinLoss(PlayerInterface.loadWinLoss().get("wins") + 1,
                    PlayerInterface.loadWinLoss().get("losses"));
            disableSubmissions();
            showGameOver("You Win! The correct word was: " + correctWord);
        } else {
            updateGuessBoxes(false);
            currentAttempt++;
            if (currentAttempt >= 6) {
                System.out.println("Game Over. The correct word was: " + correctWord);
                PlayerInterface.saveWinLoss(PlayerInterface.loadWinLoss().get("wins"),
                        PlayerInterface.loadWinLoss().get("losses") + 1);
                showGameOver("Game Over! The correct word was: " + correctWord);
            }
        }

        updateStats();
    }

    private void updateGuessBoxes(boolean isCorrect) {
        // Ensure we're working with a valid attempt index
        if (currentAttempt < 0 || currentAttempt >= currentGuessBoxes.size()) {
            return;
        }

        List<TextField> row = currentGuessBoxes.get(currentAttempt);
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
        box.setEditable(false);
    }

    private void addSymbolToBox(TextField box, String symbol) {
        StackPane pane = new StackPane();
        pane.setAlignment(Pos.TOP_RIGHT);

        ImageView symbolImage = null;
        try {
            String imagePath = "Resources/Symbols/" + symbol + ".png";
            File file = new File(imagePath);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                symbolImage = new ImageView(image);
                symbolImage.setFitWidth(20);
                symbolImage.setFitHeight(20);
            } else {
                System.out.println("Image not found: " + imagePath);
            }
        } catch (Exception e) {
            System.out.println("Error loading symbol: " + e.getMessage());
        }

        if (symbolImage != null) {
            pane.getChildren().add(symbolImage);
        }

        StackPane textFieldWithSymbol = new StackPane();
        textFieldWithSymbol.getChildren().addAll(box, pane);

        grid.add(textFieldWithSymbol, GridPane.getColumnIndex(box), GridPane.getRowIndex(box));
    }

    private void showGameOver(String message) {
        gameOverText.setText(message);
        replayButton.setVisible(true);
    }

    private void disableSubmissions() {
        submitButton.setDisable(true);
    }

    private void goHome() {
        screenManager.showScreen("HomeScreen");
    }

    private void startNewGame() {
        loadPossibleWords();
        initializeGame();
        gameOverText.setText("");
        replayButton.setVisible(false);
        submitButton.setDisable(false);
    }

    private void updateStats() {
        Map<String, Integer> winLossStats = PlayerInterface.loadWinLoss();
        int wins = winLossStats.get("wins");
        int losses = winLossStats.get("losses");

        double avgGuesses = PlayerInterface.calculateAverageGuesses();
        String mostGuessedWord = PlayerInterface.getMostGuessedWord();
        String mostGuessedLetter = String.valueOf(PlayerInterface.getMostGuessedLetter());

        winsText.setText("Wins: " + wins);
        lossesText.setText("Losses: " + losses);
        avgGuessesText.setText("Avg Guesses: " + String.format("%.2f", avgGuesses));
        mostGuessedWordText.setText("Most Guessed Word: " + mostGuessedWord);
        mostGuessedLetterText.setText("Most Guessed Letter: " + mostGuessedLetter);
    }

    public VBox getRoot() {
        return root;
    }
}
