package battleship;

import org.junit.jupiter.api.*;
import java.io.File;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for PDFExporter.
 * Author: IGE-123016
 * Date: 2026-04-16 16:00
 * Cyclomatic Complexity for each method:
 * - exportGameToPDF: 10
 */

class PDFExporterTest {

    private IGame game;
    private IMove move;
    private IPosition position;
    private IGame.ShotResult shotResult;
    private IShip ship;

    private static final String FILE = "test.pdf";

    @BeforeEach
    void setUp() {
        game = mock(IGame.class);
        move = mock(IMove.class);
        position = mock(IPosition.class);
        shotResult = mock(IGame.ShotResult.class);
        ship = mock(IShip.class);

        // Game stats
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

        // Default = MISS
        when(shotResult.valid()).thenReturn(true);
        when(shotResult.repeated()).thenReturn(false);
        when(shotResult.ship()).thenReturn(null);
    }

    @AfterEach
    void tearDown() {
        new File(FILE).delete();
    }

    @Test
    void exportGameToPDF1_shouldGeneratePdf() {
        assertDoesNotThrow(() ->
                PDFExporter.exportGameToPDF(game, FILE)
        );

        assertTrue(new File(FILE).exists(),
                "PDF should be created");
    }

    @Test
    void exportGameToPDF2_invalidShot() {
        when(shotResult.valid()).thenReturn(false);

        assertDoesNotThrow(() ->
                PDFExporter.exportGameToPDF(game, FILE)
        );
    }

    @Test
    void exportGameToPDF3_repeatedShot() {
        when(shotResult.repeated()).thenReturn(true);

        assertDoesNotThrow(() ->
                PDFExporter.exportGameToPDF(game, FILE)
        );
    }

    @Test
    void exportGameToPDF4_hitShot() {
        when(shotResult.ship()).thenReturn(ship);
        when(shotResult.sunk()).thenReturn(false);

        assertDoesNotThrow(() ->
                PDFExporter.exportGameToPDF(game, FILE)
        );
    }

    @Test
    void exportGameToPDF5_sunkShot() {
        when(shotResult.ship()).thenReturn(ship);
        when(shotResult.sunk()).thenReturn(true);

        assertDoesNotThrow(() ->
                PDFExporter.exportGameToPDF(game, FILE)
        );
    }

    @Test
    void exportGameToPDF6_multipleMoves() {
        IMove move2 = mock(IMove.class);

        when(move2.getNumber()).thenReturn(2);
        when(move2.getShots()).thenReturn(List.of(position));
        when(move2.getShotResults()).thenReturn(List.of(shotResult));

        when(game.getAlienMoves()).thenReturn(List.of(move, move2));

        assertDoesNotThrow(() ->
                PDFExporter.exportGameToPDF(game, FILE)
        );
    }

    @Test
    void exportGameToPDF7_emptyMoves() {
        when(game.getAlienMoves()).thenReturn(List.of());

        assertDoesNotThrow(() ->
                PDFExporter.exportGameToPDF(game, FILE)
        );
    }

    @Test
    void exportGameToPDF8_partialResults() {
        when(move.getShotResults()).thenReturn(List.of()); // menos que NUMBER_SHOTS

        assertDoesNotThrow(() ->
                PDFExporter.exportGameToPDF(game, FILE)
        );
    }

    @Test
    void exportGameToPDF9_nullArguments() {
        assertThrows(AssertionError.class, () ->
                PDFExporter.exportGameToPDF(null, FILE)
        );

        assertThrows(AssertionError.class, () ->
                PDFExporter.exportGameToPDF(game, null)
        );
    }

    @Test
    void shouldThrowRuntimeExceptionWhenPdfFails() {
        IGame badGame = mock(IGame.class);

        when(badGame.getAlienMoves()).thenThrow(new RuntimeException("fail"));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                PDFExporter.exportGameToPDF(badGame, "test.pdf")
        );

        assertTrue(ex.getMessage().contains("Error while generating PDF file"));
    }
}
