package battleship;

import org.openpdf.text.*;
import org.openpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.util.List;

/**
 * PDFExporter provides functionality to generate a PDF report of a Battleship game.
 * <p>
 * The report includes a summary of the game statistics, a detailed table of all moves
 * performed by the players, and the results of each shot (hit, miss, sunk ship, invalid, or repeated).
 * </p>
 * <p>
 * This class uses the OpenPDF library (com.github.librepdf:openpdf) for PDF generation and works with
 * any implementation of {@link IGame}, {@link IMove}, and {@link IGame.ShotResult}.
 * </p>
 */
public class PDFExporter {

    /**
     * Generates a PDF file containing a summary of the game and detailed moves.
     *
     * @param game     The instance of {@link IGame} containing all game state and moves.
     * @param fileName The name of the PDF file to be created (e.g., "jogadas.pdf").
     * @throws RuntimeException if there is any error during PDF creation or file writing.
     */
    public static void exportGameToPDF(IGame game, String fileName) {

        assert game != null;
        assert fileName != null && !fileName.isEmpty();

        try {
            // Create the document and link to a file output stream
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            addTitle(document);

            addGameStatistics(game, document);

            // Create a table for all moves
            addMovesTable(game, document);

            // Close the document
            document.close();

            System.out.println("PDF successfully generated: " + fileName);

        } catch (Exception e) {
            throw new RuntimeException("Error while generating PDF file: " + fileName, e);
        }
    }

    private static void addMovesTable(IGame game, Document document) {
        PdfPTable table = new PdfPTable(5); // Columns: Turn | Shots | Result1 | Result2 | Result3
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3, 2, 2, 2});

        // Table header
        table.addCell("Turn");
        table.addCell("Shots");
        table.addCell("Result 1");
        table.addCell("Result 2");
        table.addCell("Result 3");

        // Populate the table with moves
        List<IMove> moves = game.getAlienMoves(); // All enemy moves stored in the game
        for (IMove move : moves) {
            // Turn number
            table.addCell(String.valueOf(move.getNumber()));

            // Shots as a single string, e.g., "(2,3) (4,5) (1,1)"
            StringBuilder shots = new StringBuilder();
            for (IPosition pos : move.getShots()) {
                shots.append("(").append(pos.getClassicRow())
                        .append(",").append(pos.getClassicColumn()).append(") ");
            }
            table.addCell(shots.toString().trim());

            // Results of each shot
            List<IGame.ShotResult> results = move.getShotResults();
            for (int i = 0; i < Game.NUMBER_SHOTS; i++) {
                if (i < results.size()) {
                    IGame.ShotResult res = results.get(i);
                    if (!res.valid())
                        table.addCell("Invalid");
                    else if (res.repeated())
                        table.addCell("Repeated");
                    else if (res.ship() != null && res.sunk())
                        table.addCell("Sunk");
                    else if (res.ship() != null)
                        table.addCell("Hit");
                    else
                        table.addCell("Miss");
                } else {
                    table.addCell("-");
                }
            }
        }

        document.add(table);
    }

    private static void addGameStatistics(IGame game, Document document) {
        // Add general game statistics
        document.add(new Paragraph("Remaining Ships: " + game.getRemainingShips()));
        document.add(new Paragraph("Repeated Shots: " + game.getRepeatedShots()));
        document.add(new Paragraph("Invalid Shots: " + game.getInvalidShots()));
        document.add(new Paragraph("Successful Hits: " + game.getHits()));
        document.add(new Paragraph("Sunk Ships: " + game.getSunkShips()));

        document.add(new Paragraph(" ")); // blank line
    }

    private static void addTitle(Document document) {
        // Add the title centered at the top
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Battleship Game Summary", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" ")); // blank line
    }
}