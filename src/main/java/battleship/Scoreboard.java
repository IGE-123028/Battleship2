package battleship;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Utility class for saving game results to a CSV file.
 */
public class Scoreboard {

    private Scoreboard() {
        // Utility class
    }

    /**
     * Saves the game result to the scoreboard file.
     *
     * @param resultado the result string to save
     */
    public static void saveResult(String resultado) {
        String filePath = getFilePath();
        writeResultToCSV(resultado, filePath);
    }

    private static void writeResultToCSV(String resultado, String filePath) {
        try (FileWriter writer = new FileWriter(filePath, true);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            csvPrinter.printRecord(LocalDateTime.now(), resultado);

        } catch (IOException e) {
            System.out.println("Erro ao guardar scoreboard.");
        }
    }

    private static @NotNull String getFilePath() {
        String homeDir = System.getProperty("user.home");
        return homeDir + java.io.File.separator + "battleship_scoreboard.csv";
    }
}
