package battleship;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Scoreboard {

    public static void saveResult(String resultado) {
        try (FileWriter writer = new FileWriter("data/scoreboard.csv", true);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            csvPrinter.printRecord(LocalDateTime.now(), resultado);

        } catch (IOException e) {
            System.out.println("Erro ao guardar scoreboard.");
        }
    }
}