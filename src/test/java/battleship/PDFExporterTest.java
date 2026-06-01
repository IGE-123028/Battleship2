package battleship;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.openpdf.text.Document;
import org.openpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PDFExporterTest {

    private IGame game;
    private IMove move;
    private IPosition position;
    private IGame.ShotResult shotResult;
    private IShip ship;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        game = mock(IGame.class);
        move = mock(IMove.class);
        position = mock(IPosition.class);
        shotResult = mock(IGame.ShotResult.class);
        ship = mock(IShip.class);

        // Default Game stats
        when(game.getRemainingShips()).thenReturn(3);
        when(game.getRepeatedShots()).thenReturn(1);
        when(game.getInvalidShots()).thenReturn(0);
        when(game.getHits()).thenReturn(2);
        when(game.getSunkShips()).thenReturn(1);

        // Default Move and Shot context
        when(move.getNumber()).thenReturn(1);
        when(position.getClassicRow()).thenReturn('A');
        when(position.getClassicColumn()).thenReturn(1);
        when(move.getShots()).thenReturn(List.of(position));
        when(move.getShotResults()).thenReturn(List.of(shotResult));
        when(game.getAlienMoves()).thenReturn(List.of(move));
    }

    @AfterEach
    void tearDown() {
        game = null;
        move = null;
        position = null;
        shotResult = null;
    }

    private String extractPdfText(String filePath) {
        // Mocking or a real PDF library could be used here to verify content.
        // For simplicity, we just check if the file exists and is not empty.
        java.io.File file = new java.io.File(filePath);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
        return "PDF Generated";
    }

    @Test
    @DisplayName("exportGameToPDF - Path 1: game == null throws AssertionError")
    void exportGameToPDF1() {
        assertThrows(AssertionError.class, () -> PDFExporter.exportGameToPDF(null, "test.pdf"));
    }

    @Test
    @DisplayName("exportGameToPDF - Path 2: fileName == null throws AssertionError")
    void exportGameToPDF2() {
        assertThrows(AssertionError.class, () -> PDFExporter.exportGameToPDF(game, null));
    }

    @Test
    @DisplayName("exportGameToPDF - Path 3: empty fileName throws AssertionError")
    void exportGameToPDF3() {
        assertThrows(AssertionError.class, () -> PDFExporter.exportGameToPDF(game, ""));
    }

    @Test
    @DisplayName("exportGameToPDF - Path 4: valid game and fileName")
    void exportGameToPDF4() {
        String fileName = tempDir.resolve("test4.pdf").toString();
        assertDoesNotThrow(() -> PDFExporter.exportGameToPDF(game, fileName));
        extractPdfText(fileName);
    }

    @Test
    @DisplayName("exportGameToPDF - Path 5: game with multiple moves")
    void exportGameToPDF5() {
        when(game.getAlienMoves()).thenReturn(List.of(move, move));
        String fileName = tempDir.resolve("test5.pdf").toString();
        assertDoesNotThrow(() -> PDFExporter.exportGameToPDF(game, fileName));
        extractPdfText(fileName);
    }

    @Test
    @DisplayName("exportGameToPDF - Path 6: move with multiple shots")
    void exportGameToPDF6() {
        when(move.getShots()).thenReturn(List.of(position, position, position));
        when(move.getShotResults()).thenReturn(List.of(shotResult, shotResult, shotResult));
        String fileName = tempDir.resolve("test6.pdf").toString();
        assertDoesNotThrow(() -> PDFExporter.exportGameToPDF(game, fileName));
        extractPdfText(fileName);
    }

    @Test
    @DisplayName("exportGameToPDF - Path 7: different shot results")
    void exportGameToPDF7() {
        IGame.ShotResult res1 = mock(IGame.ShotResult.class);
        IGame.ShotResult res2 = mock(IGame.ShotResult.class);
        IGame.ShotResult res3 = mock(IGame.ShotResult.class);

        when(res1.valid()).thenReturn(false);
        when(res2.valid()).thenReturn(true);
        when(res2.repeated()).thenReturn(true);
        when(res3.valid()).thenReturn(true);
        when(res3.repeated()).thenReturn(false);
        when(res3.ship()).thenReturn(ship);
        when(res3.sunk()).thenReturn(true);

        when(move.getShotResults()).thenReturn(List.of(res1, res2, res3));
        when(move.getShots()).thenReturn(List.of(position, position, position));

        String fileName = tempDir.resolve("test7.pdf").toString();
        assertDoesNotThrow(() -> PDFExporter.exportGameToPDF(game, fileName));
        extractPdfText(fileName);
    }

    @Test
    @DisplayName("exportGameToPDF - Path 8: shot that is a hit but not sunk")
    void exportGameToPDF8() {
        when(shotResult.valid()).thenReturn(true);
        when(shotResult.repeated()).thenReturn(false);
        when(shotResult.ship()).thenReturn(ship);
        when(shotResult.sunk()).thenReturn(false);

        String fileName = tempDir.resolve("test8.pdf").toString();
        assertDoesNotThrow(() -> PDFExporter.exportGameToPDF(game, fileName));
        extractPdfText(fileName);
    }

    @Test
    @DisplayName("exportGameToPDF - Path 9: shot that is a miss")
    void exportGameToPDF9() {
        when(shotResult.valid()).thenReturn(true);
        when(shotResult.repeated()).thenReturn(false);
        when(shotResult.ship()).thenReturn(null);

        String fileName = tempDir.resolve("test9.pdf").toString();
        assertDoesNotThrow(() -> PDFExporter.exportGameToPDF(game, fileName));
        extractPdfText(fileName);
    }

    @Test
    @DisplayName("exportGameToPDF - Path 10: exception during PDF generation")
    void exportGameToPDF10() {
        // Using a non-existent or restricted directory to force an exception
        String fileName = "/invalid_path/test10.pdf";
        assertThrows(RuntimeException.class, () -> PDFExporter.exportGameToPDF(game, fileName));
    }
}
