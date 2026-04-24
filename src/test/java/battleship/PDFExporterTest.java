package battleship;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for PDFExporter.
 * Author: IGE-123016
 * Date: 2026-04-24 21:40
 * Cyclomatic Complexity:
 * - PDFExporter: 1
 * - exportGameToPDF: 10
 */
@DisplayName("Tests for PDFExporter")
class PDFExporterTest {

    private PDFExporter pdfExporter;
    private IGame game;
    private IMove move;
    private IPosition position;
    private IGame.ShotResult shotResult;
    private IShip ship;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        pdfExporter = new PDFExporter();

        game = mock(IGame.class);
        move = mock(IMove.class);
        position = mock(IPosition.class);
        shotResult = mock(IGame.ShotResult.class);
        ship = mock(IShip.class);

        // Default Game stats
        when(game.getRemainingShips()).thenReturn(3);
        when(game.getRepeatedShots()).thenReturn(1);
        when(game.getInvalidShots()).thenReturn(0);
        when(game.getHits()).thenReturn(5);
        when(game.getSunkShips()).thenReturn(2);

        // Move + Position
        when(move.getNumber()).thenReturn(1);
        when(position.getClassicRow()).thenReturn('C');
        when(position.getClassicColumn()).thenReturn(3);

        when(move.getShots()).thenReturn(List.of(position));
        when(move.getShotResults()).thenReturn(List.of(shotResult));

        when(game.getAlienMoves()).thenReturn(List.of(move));
    }

    @AfterEach
    void tearDown() {
        pdfExporter = null;
        game = null;
        move = null;
        position = null;
        shotResult = null;
        ship = null;
    }

    private String extractPdfText(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists())
            return "";
        try (PDDocument doc = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(doc);
        }
    }

    @Test
    @DisplayName("PDFExporter constructor - Path 1")
    void PDFExporter1() {
        assertNotNull(pdfExporter, "Error: expected pdfExporter instance to not be null");
    }

    @Test
    @DisplayName("exportGameToPDF - Path 1: game == null throws AssertionError")
    void exportGameToPDF1() {
        String file = tempDir.resolve("test1.pdf").toString();
        AssertionError exception = assertThrows(AssertionError.class, () -> PDFExporter.exportGameToPDF(null, file),
                "Error: expected AssertionError when game is null");
        assertNotNull(exception, "Error: expected an exception to be thrown");
    }

    @Test
    @DisplayName("exportGameToPDF - Path 2: fileName == null or empty throws AssertionError")
    void exportGameToPDF2() {
        AssertionError exception = assertThrows(AssertionError.class, () -> PDFExporter.exportGameToPDF(game, null),
                "Error: expected AssertionError when fileName is null");
        assertNotNull(exception, "Error: expected an exception to be thrown");

        AssertionError exception2 = assertThrows(AssertionError.class, () -> PDFExporter.exportGameToPDF(game, ""),
                "Error: expected AssertionError when fileName is empty");
        assertNotNull(exception2, "Error: expected an exception to be thrown for empty fileName");
    }

    @Test
    @DisplayName("exportGameToPDF - Path 3: Exception during PDF generation throws RuntimeException")
    void exportGameToPDF3() {
        // Pass the temp directory path instead of a file. 
        // FileOutputStream will throw a FileNotFoundException (Access is denied), 
        // triggering the catch block without creating an unclosable file lock.
        String file = tempDir.toString();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> PDFExporter.exportGameToPDF(game, file),
                "Error: expected RuntimeException when an internal error occurs");

        assertTrue(exception.getMessage().contains("Error while generating PDF file"),
                "Error: expected RuntimeException message to be wrapped correctly");
    }

    @Test
    @DisplayName("exportGameToPDF - Path 4: empty moves list generates basic PDF")
    void exportGameToPDF4() throws IOException {
        when(game.getAlienMoves()).thenReturn(Collections.emptyList());
        String file = tempDir.resolve("test4.pdf").toString();

        assertDoesNotThrow(() -> PDFExporter.exportGameToPDF(game, file),
                "Error: exportGameToPDF threw an exception for empty move list");

        String pdfText = extractPdfText(file);
        assertAll("pdf-content",
                () -> assertTrue(pdfText.contains("Remaining Ships: 3"), "Error: expected summary stats in PDF"),
                () -> assertTrue(pdfText.contains("Turn"), "Error: expected table headers in PDF"),
                () -> assertFalse(pdfText.contains("Miss"), "Error: expected no shot results in PDF"));
    }

    @Test
    @DisplayName("exportGameToPDF - Path 5: i >= results.size() prints '-'")
    void exportGameToPDF5() throws IOException {
        when(move.getShotResults()).thenReturn(Collections.emptyList());
        String file = tempDir.resolve("test5.pdf").toString();

        assertDoesNotThrow(() -> PDFExporter.exportGameToPDF(game, file),
                "Error: exportGameToPDF threw an exception for empty shot results");

        String pdfText = extractPdfText(file);
        assertTrue(pdfText.contains("-"), "Error: expected '-' in PDF for missing shot results");
    }

    @Test
    @DisplayName("exportGameToPDF - Path 6: !res.valid() prints 'Invalid'")
    void exportGameToPDF6() throws IOException {
        when(shotResult.valid()).thenReturn(false);
        String file = tempDir.resolve("test6.pdf").toString();

        assertDoesNotThrow(() -> PDFExporter.exportGameToPDF(game, file),
                "Error: exportGameToPDF threw an exception for invalid shot");

        String pdfText = extractPdfText(file);
        assertTrue(pdfText.contains("Invalid"), "Error: expected 'Invalid' in PDF");
    }

    @Test
    @DisplayName("exportGameToPDF - Path 7: res.repeated() prints 'Repeated'")
    void exportGameToPDF7() throws IOException {
        when(shotResult.valid()).thenReturn(true);
        when(shotResult.repeated()).thenReturn(true);
        String file = tempDir.resolve("test7.pdf").toString();

        assertDoesNotThrow(() -> PDFExporter.exportGameToPDF(game, file),
                "Error: exportGameToPDF threw an exception for repeated shot");

        String pdfText = extractPdfText(file);
        assertTrue(pdfText.contains("Repeated"), "Error: expected 'Repeated' in PDF");
    }

    @Test
    @DisplayName("exportGameToPDF - Path 8: res.ship() != null && res.sunk() prints 'Sunk'")
    void exportGameToPDF8() throws IOException {
        when(shotResult.valid()).thenReturn(true);
        when(shotResult.repeated()).thenReturn(false);
        when(shotResult.ship()).thenReturn(ship);
        when(shotResult.sunk()).thenReturn(true);
        String file = tempDir.resolve("test8.pdf").toString();

        assertDoesNotThrow(() -> PDFExporter.exportGameToPDF(game, file),
                "Error: exportGameToPDF threw an exception for sunk ship");

        String pdfText = extractPdfText(file);
        assertTrue(pdfText.contains("Sunk"), "Error: expected 'Sunk' in PDF");
    }

    @Test
    @DisplayName("exportGameToPDF - Path 9: res.ship() != null && !res.sunk() prints 'Hit'")
    void exportGameToPDF9() throws IOException {
        when(shotResult.valid()).thenReturn(true);
        when(shotResult.repeated()).thenReturn(false);
        when(shotResult.ship()).thenReturn(ship);
        when(shotResult.sunk()).thenReturn(false);
        String file = tempDir.resolve("test9.pdf").toString();

        assertDoesNotThrow(() -> PDFExporter.exportGameToPDF(game, file),
                "Error: exportGameToPDF threw an exception for hit ship");

        String pdfText = extractPdfText(file);
        assertTrue(pdfText.contains("Hit"), "Error: expected 'Hit' in PDF");
    }

    @Test
    @DisplayName("exportGameToPDF - Path 10: res.ship() == null prints 'Miss'")
    void exportGameToPDF10() throws IOException {
        when(shotResult.valid()).thenReturn(true);
        when(shotResult.repeated()).thenReturn(false);
        when(shotResult.ship()).thenReturn(null);
        String file = tempDir.resolve("test10.pdf").toString();

        assertDoesNotThrow(() -> PDFExporter.exportGameToPDF(game, file),
                "Error: exportGameToPDF threw an exception for miss");

        String pdfText = extractPdfText(file);
        assertAll("pdf-miss-content",
                () -> assertTrue(pdfText.contains("Miss"), "Error: expected 'Miss' in PDF"),
                () -> assertTrue(pdfText.contains("(C,3)"), "Error: expected coordinates in PDF"));
    }
}
