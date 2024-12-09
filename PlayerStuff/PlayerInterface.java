package PlayerStuff;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PlayerInterface {

    private static final String SAVE_DIR = "Resources/SaveFiles";
    private static final String WIN_LOSS_FILE = SAVE_DIR + "/win_loss.txt";
    private static final String TOTAL_GUESSES_FILE = SAVE_DIR + "/total_guesses.txt";
    private static final String GUESSED_WORDS_FILE = SAVE_DIR + "/guessed_words.txt";

    // Ensure the directory exists
    private static void ensureDirectoryExists() {
        Path path = Paths.get(SAVE_DIR);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                System.out.println("Error creating save directory: " + e.getMessage());
            }
        }
    }

    // Load win and loss count
    public static Map<String, Integer> loadWinLoss() {
        ensureDirectoryExists();
        Map<String, Integer> winLoss = new HashMap<>();
        try {
            Path path = Paths.get(WIN_LOSS_FILE);
            if (Files.exists(path)) {
                List<String> lines = Files.readAllLines(path);
                for (String line : lines) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        String key = parts[0].trim().toLowerCase();
                        String value = parts[1].trim();
                        try {
                            int count = Integer.parseInt(value);
                            winLoss.put(key, count);
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing value for " + key + ": " + e.getMessage());
                        }
                    }
                }
            } else {
                winLoss.put("wins", 0);
                winLoss.put("losses", 0);
            }
        } catch (IOException e) {
            System.out.println("Error reading win/loss file: " + e.getMessage());
        }
        return winLoss;
    }

    // Save win/loss data
    public static void saveWinLoss(int wins, int losses) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(WIN_LOSS_FILE))) {
            writer.write("wins=" + wins);
            writer.newLine();
            writer.write("losses=" + losses);
        } catch (IOException e) {
            System.out.println("Error writing win/loss file: " + e.getMessage());
        }
    }

    // Load total guesses
    public static int loadTotalGuesses() {
        ensureDirectoryExists();
        try {
            Path path = Paths.get(TOTAL_GUESSES_FILE);
            if (Files.exists(path)) {
                List<String> lines = Files.readAllLines(path);
                if (!lines.isEmpty()) {
                    return Integer.parseInt(lines.get(0).trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading total guesses file: " + e.getMessage());
        }
        return 0;
    }

    // Save total guesses
    public static void saveTotalGuesses(int totalGuesses) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TOTAL_GUESSES_FILE))) {
            writer.write(String.valueOf(totalGuesses));
        } catch (IOException e) {
            System.out.println("Error writing total guesses file: " + e.getMessage());
        }
    }

    // Load guessed words
    public static List<String> loadGuessedWords() {
        ensureDirectoryExists();
        List<String> guessedWords = new ArrayList<>();
        try {
            Path path = Paths.get(GUESSED_WORDS_FILE);
            if (Files.exists(path)) {
                guessedWords = Files.readAllLines(path);
            }
        } catch (IOException e) {
            System.out.println("Error reading guessed words file: " + e.getMessage());
        }
        return guessedWords;
    }

    // Save guessed word
    public static void saveGuessedWord(String word) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GUESSED_WORDS_FILE, true))) {
            writer.write(word);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing guessed words file: " + e.getMessage());
        }
    }

    // Calculate average guesses
    public static double calculateAverageGuesses() {
        int totalGuesses = loadTotalGuesses();
        List<String> guessedWords = loadGuessedWords();
        return guessedWords.isEmpty() ? 0 : (double) totalGuesses / guessedWords.size();
    }

    // Get the most guessed word
    public static String getMostGuessedWord() {
        List<String> guessedWords = loadGuessedWords();
        Map<String, Integer> wordCount = new HashMap<>();
        for (String word : guessedWords) {
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }
        return wordCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }

    // Get the most guessed letter
    public static char getMostGuessedLetter() {
        List<String> guessedWords = loadGuessedWords();
        Map<Character, Integer> letterCount = new HashMap<>();
        for (String word : guessedWords) {
            for (char c : word.toCharArray()) {
                letterCount.put(c, letterCount.getOrDefault(c, 0) + 1);
            }
        }
        return letterCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse('N');
    }
}

