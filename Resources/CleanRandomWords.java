import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class CleanRandomWords {

    public static void main(String[] args) {
        String fileData = "";
        try {
            File myFile = new File("RandomStanderedGameWords.txt");
            Scanner myReader = new Scanner(myFile);
            while(myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.length() > 3) {
                    fileData += data + "\n";
                }

            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("AN ERROR OCCURED.");
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter("RandomStanderedGameWords.txt")) {
            writer.write(fileData);
        } catch (IOException e) {
            System.out.println("ERROR WRITING TO FILE: " + e.getMessage());
        }

    }

}
