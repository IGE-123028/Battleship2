package battleship;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for class Game.
 * Author: manas
 * Date: 2026-04-26 12:46
 * Cyclomatic Complexity:
 * - constructor: 1
 * - printBoard(...): 7
 * - jsonShots(...): 2
 * - getMyFleet(): 1
 * - getAlienMoves(): 1
 * - getAlienFleet(): 1
 * - getMyMoves(): 1
 * - randomEnemyFire(): 4
 * - readEnemyFire(...): 5
 * - fireShots(...): 3
 * - fireMyShots(...): 3
 * - fireSingleShot(...): 6
 * - fireMySingleShot(...): 6
 * - getRepeatedShots(): 1
 * - getInvalidShots(): 1
 * - getHits(): 1
 * - getSunkShips(): 1
 * - getRemainingShips(): 1
 * - getRemainingAlienShips(): 1
 * - repeatedShot(...): 2
 * - myRepeatedShot(...): 2
 * - printMyBoard(...): 1
 * - printAlienBoard(...): 1
 * - over(): 1
 */
class GameTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Fleet myFleet;
    private Barge myBarge;
    private Caravel myCaravel;
    private Game game;

    @BeforeEach
    void setUp() throws Exception {
        Messages.load("en");

        myFleet = new Fleet();
        myBarge = new Barge(Compass.NORTH, new Position(1, 1));
        myCaravel = new Caravel(Compass.EAST, new Position(3, 3));

        assertTrue(myFleet.addShip(myBarge),
                "Error: expected the setup fleet to accept the barge but addShip returned false.");
        assertTrue(myFleet.addShip(myCaravel),
                "Error: expected the setup fleet to accept the caravel but addShip returned false.");

        game = new Game(myFleet);
    }

    @AfterEach
    void tearDown() {
        game = null;
        myFleet = null;
        myBarge = null;
        myCaravel = null;

        assertAll(
                () -> assertNull(game, "Error: expected game to be null after teardown but it was not null."),
                () -> assertNull(myFleet, "Error: expected myFleet to be null after teardown but it was not null."),
                () -> assertNull(myBarge, "Error: expected myBarge to be null after teardown but it was not null."),
                () -> assertNull(myCaravel, "Error: expected myCaravel to be null after teardown but it was not null.")
        );
    }

    @Test
    void constructor() {
        assertAll(
                () -> assertNotNull(game, "Error: expected Game to be created in setup but it was null."),
                () -> assertSame(myFleet, game.getMyFleet(),
                        "Error: expected Game to keep the provided fleet instance but it returned a different object."),
                () -> assertTrue(game.getAlienMoves().isEmpty(),
                        "Error: expected alienMoves to start empty but it was not empty."),
                () -> assertTrue(game.getMyMoves().isEmpty(),
                        "Error: expected myMoves to start empty but it was not empty."),
                () -> assertEquals(0, game.getInvalidShots(),
                        "Error: expected invalid shot count to start at 0 but it had a different value."),
                () -> assertEquals(0, game.getRepeatedShots(),
                        "Error: expected repeated shot count to start at 0 but it had a different value."),
                () -> assertEquals(0, game.getHits(),
                        "Error: expected hit count to start at 0 but it had a different value."),
                () -> assertEquals(0, game.getSunkShips(),
                        "Error: expected sunk ship count to start at 0 but it had a different value.")
        );
    }

    @Test
    void printBoard1() {
        String output = captureOutput(() -> Game.printBoard(myFleet, List.of(), false, false, false));

        assertTrue(output.contains(BoardColor.ship()),
                "Error: expected printBoard to render visible ships when hide_ships is false but no ship marker was printed.");
    }

    @Test
    void printBoard2() {
        String output = captureOutput(() -> Game.printBoard(myFleet, List.of(), false, false, true));

        assertFalse(output.contains(BoardColor.ship()),
                "Error: expected printBoard to hide floating ships when hide_ships is true but a ship marker was printed.");
    }

    @Test
    void printBoard3() {
        myBarge.sink();

        String output = captureOutput(() -> Game.printBoard(myFleet, List.of(), false, false, true));

        assertAll(
                () -> assertTrue(output.contains(BoardColor.ship()),
                        "Error: expected printBoard to show sunk ships even when hide_ships is true but no sunk ship marker was printed."),
                () -> assertTrue(output.contains(BoardColor.adjacent()),
                        "Error: expected printBoard to show adjacent markers for sunk ships but no adjacent marker was printed.")
        );
    }

    @Test
    void printBoard4() {
        IPosition hitShot = myBarge.getPositions().get(0);
        IMove move = new Move(1, List.of(hitShot), new ArrayList<>());

        String output = captureOutput(() -> Game.printBoard(myFleet, List.of(move), true, false, false));

        assertTrue(output.contains(BoardColor.hit()),
                "Error: expected printBoard to render hit shots when show_shots is true but no hit marker was printed.");
    }

    @Test
    void printBoard5() {
        IPosition waterShot = firstWaterPosition(myFleet);
        IMove move = new Move(1, List.of(waterShot), new ArrayList<>());

        String output = captureOutput(() -> Game.printBoard(myFleet, List.of(move), true, false, false));

        assertTrue(output.contains(BoardColor.miss()),
                "Error: expected printBoard to render missed shots when show_shots is true but no miss marker was printed.");
    }

    @Test
    void printBoard6() {
        Fleet emptyFleet = new Fleet();
        IMove move = new Move(1, List.of(new Position(-1, 0)), new ArrayList<>());

        String output = captureOutput(() -> Game.printBoard(emptyFleet, List.of(move), true, false, false));

        assertAll(
                () -> assertFalse(output.contains(BoardColor.hit()),
                        "Error: expected printBoard to ignore outside shots but it printed a hit marker."),
                () -> assertFalse(output.contains(BoardColor.miss()),
                        "Error: expected printBoard to ignore outside shots but it printed a miss marker.")
        );
    }

    @Test
    void printBoard7() {
        String output = captureOutput(() -> Game.printBoard(myFleet, List.of(), false, true, false));

        assertAll(
                () -> assertTrue(output.contains("LEGENDA"),
                        "Error: expected printBoard to render the legend header when showLegend is true but it was missing."),
                () -> assertTrue(output.contains("'#'->navio"),
                        "Error: expected printBoard to include the legend text for ship markers but it was missing.")
        );
    }

    @Test
    void jsonShots1() throws Exception {
        List<Map<String, Object>> parsed = parseJsonArray(Game.jsonShots(List.of()));

        assertEquals(0, parsed.size(),
                "Error: expected jsonShots to serialize an empty list as an empty JSON array but it had a different size.");
    }

    @Test
    void jsonShots2() throws Exception {
        String json = Game.jsonShots(List.of(new Position('A', 1), new Position('C', 4)));
        List<Map<String, Object>> parsed = parseJsonArray(json);

        assertAll(
                () -> assertEquals(2, parsed.size(),
                        "Error: expected jsonShots to serialize two positions but the JSON array size was different."),
                () -> assertEquals("A", parsed.get(0).get("row"),
                        "Error: expected the first serialized shot row to be 'A' but it was different."),
                () -> assertEquals(1, parsed.get(0).get("column"),
                        "Error: expected the first serialized shot column to be 1 but it was different."),
                () -> assertEquals("C", parsed.get(1).get("row"),
                        "Error: expected the second serialized shot row to be 'C' but it was different."),
                () -> assertEquals(4, parsed.get(1).get("column"),
                        "Error: expected the second serialized shot column to be 4 but it was different.")
        );
    }

    @Test
    void jsonShots3() {
        try (MockedConstruction<ObjectMapper> ignored = mockConstruction(ObjectMapper.class,
                (mock, context) -> when(mock.writeValueAsString(any())).thenThrow(
                        new com.fasterxml.jackson.core.JsonProcessingException("forced failure") {
                        }))) {
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> Game.jsonShots(List.of(new Position('A', 1))),
                    "Error: expected jsonShots() to wrap JSON serialization failures in a RuntimeException but no exception was thrown.");

            assertAll(
                    () -> assertEquals("Erro ao serializar o JSON", exception.getMessage(),
                            "Error: expected jsonShots() to preserve the RuntimeException message for serialization errors but it was different."),
                    () -> assertTrue(exception.getCause() instanceof com.fasterxml.jackson.core.JsonProcessingException,
                            "Error: expected jsonShots() to keep the JsonProcessingException as the cause but it did not.")
            );
        }
    }

    @Test
    void getMyFleet() {
        assertSame(myFleet, game.getMyFleet(),
                "Error: expected getMyFleet() to return the fleet passed to the constructor but it returned a different object.");
    }

    @Test
    void getAlienMoves() {
        assertTrue(game.getAlienMoves().isEmpty(),
                "Error: expected getAlienMoves() to return an empty list before any enemy fire but it was not empty.");
    }

    @Test
    void getAlienFleet() {
        assertSame(myFleet, game.getAlienFleet(),
                "Error: expected getAlienFleet() to return the current implementation result but it returned a different object.");
    }

    @Test
    void getMyMoves() {
        assertTrue(game.getMyMoves().isEmpty(),
                "Error: expected getMyMoves() to return an empty list before any player fire but it was not empty.");
    }

    @Test
    void randomEnemyFire1() throws Exception {
        try (MockedConstruction<Random> ignored = mockConstruction(Random.class,
                (mock, context) -> when(mock.nextInt(anyInt())).thenReturn(0, 0, 1, 2))) {
            String json = game.randomEnemyFire();
            List<Map<String, Object>> parsed = parseJsonArray(json);
            Set<String> uniqueShots = new HashSet<>();
            for (Map<String, Object> shot : parsed) {
                uniqueShots.add(shot.get("row") + ":" + shot.get("column"));
            }

            assertAll(
                    () -> assertEquals(Game.NUMBER_SHOTS, parsed.size(),
                            "Error: expected randomEnemyFire() to generate exactly NUMBER_SHOTS entries but it generated a different amount."),
                    () -> assertEquals(Game.NUMBER_SHOTS, uniqueShots.size(),
                            "Error: expected randomEnemyFire() to generate unique shots when enough positions exist but duplicates were produced."),
                    () -> assertEquals(1, game.getAlienMoves().size(),
                            "Error: expected randomEnemyFire() to register one alien move but the move count was different.")
            );
        }
    }

    @Test
    void randomEnemyFire2() throws Exception {
        IFleet missOnlyFleet = mockFleetWithNoShips();
        Game limitedGame = new Game(missOnlyFleet);

        Set<IPosition> remaining = Set.of(new Position(9, 8), new Position(9, 9));
        limitedGame.getAlienMoves().add(new Move(1, boardPositionsExcept(remaining), new ArrayList<>()));

        try (MockedConstruction<Random> ignored = mockConstruction(Random.class,
                (mock, context) -> when(mock.nextInt(anyInt())).thenReturn(0, 0, 1))) {
            String json = limitedGame.randomEnemyFire();
            List<Map<String, Object>> parsed = parseJsonArray(json);
            Map<String, Integer> occurrences = shotOccurrences(parsed);

            assertAll(
                    () -> assertEquals(Game.NUMBER_SHOTS, parsed.size(),
                            "Error: expected randomEnemyFire() to still return NUMBER_SHOTS entries when candidates are scarce but it did not."),
                    () -> assertEquals(2, occurrences.size(),
                            "Error: expected randomEnemyFire() to keep the two available candidates before repeating the last one but it did not."),
                    () -> assertTrue(occurrences.containsValue(1),
                            "Error: expected randomEnemyFire() to keep one scarce candidate only once before padding but it did not."),
                    () -> assertTrue(occurrences.containsValue(2),
                            "Error: expected randomEnemyFire() to repeat the last generated candidate when fewer than NUMBER_SHOTS remain but it did not.")
            );
        }
    }

    @Test
    void randomEnemyFire3() throws Exception {
        IFleet missOnlyFleet = mockFleetWithNoShips();
        Game filteredGame = new Game(missOnlyFleet);

        IPosition allowed = new Position(8, 8);
        filteredGame.getAlienMoves().add(new Move(1, boardPositionsExcept(Set.of(allowed)), new ArrayList<>()));

        String json = filteredGame.randomEnemyFire();
        List<Map<String, Object>> parsed = parseJsonArray(json);

        assertTrue(parsed.stream().allMatch(entry -> "I".equals(entry.get("row")) && Integer.valueOf(9).equals(entry.get("column"))),
                "Error: expected randomEnemyFire() to avoid previously used alien shots but it returned a blocked position.");
    }

    @Test
    void randomEnemyFire4() throws Exception {
        IFleet filteredFleet = mockFleetWithNoShips();
        IShip sunkShip = mock(IShip.class);
        List<IPosition> forbidden = List.of(new Position(0, 0), new Position(0, 1), new Position(1, 0));
        when(sunkShip.getAdjacentPositions()).thenReturn(forbidden);
        when(filteredFleet.getSunkShips()).thenReturn(List.of(sunkShip));

        Game filteredGame = new Game(filteredFleet);
        IPosition allowed = new Position(9, 9);

        Set<IPosition> blocked = new HashSet<>(boardPositionsExcept(Set.of(allowed)));
        blocked.removeAll(forbidden);
        filteredGame.getAlienMoves().add(new Move(1, new ArrayList<>(blocked), new ArrayList<>()));

        String json = filteredGame.randomEnemyFire();
        List<Map<String, Object>> parsed = parseJsonArray(json);

        assertTrue(parsed.stream().allMatch(entry -> "J".equals(entry.get("row")) && Integer.valueOf(10).equals(entry.get("column"))),
                "Error: expected randomEnemyFire() to exclude positions adjacent to sunk ships but it returned a forbidden coordinate.");
    }

    @Test
    void readEnemyFire1() throws Exception {
        String json = game.readEnemyFire(new Scanner("A 1 B 2 C 3\n"));
        List<Map<String, Object>> parsed = parseJsonArray(json);

        assertAll(
                () -> assertEquals(1, game.getAlienMoves().size(),
                        "Error: expected readEnemyFire() to register one move for valid spaced input but the move count was different."),
                () -> assertEquals(3, parsed.size(),
                        "Error: expected readEnemyFire() to parse three spaced coordinates but the parsed size was different.")
        );
    }

    @Test
    void readEnemyFire2() throws Exception {
        String json = game.readEnemyFire(new Scanner("A1 B2 C3\n"));
        List<Map<String, Object>> parsed = parseJsonArray(json);

        assertEquals(3, parsed.size(),
                "Error: expected readEnemyFire() to parse three compact coordinates but the parsed size was different.");
    }

    @Test
    void readEnemyFire3() throws Exception {
        String json = game.readEnemyFire(new Scanner("A1 B 2 C3\n"));
        List<Map<String, Object>> parsed = parseJsonArray(json);

        assertEquals(3, parsed.size(),
                "Error: expected readEnemyFire() to parse mixed compact and spaced coordinates but the parsed size was different.");
    }

    @Test
    void readEnemyFire4() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> game.readEnemyFire(new Scanner("A B2 C3\n")),
                "Error: expected readEnemyFire() to reject an incomplete column token but no exception was thrown.");

        assertTrue(exception.getMessage().contains("Posição incompleta"),
                "Error: expected readEnemyFire() to report an incomplete position but the exception message was different.");
    }

    @Test
    void readEnemyFire5() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> game.readEnemyFire(new Scanner("A1 B2\n")),
                "Error: expected readEnemyFire() to reject inputs with fewer than NUMBER_SHOTS positions but no exception was thrown.");

        assertTrue(exception.getMessage().contains("exatamente"),
                "Error: expected readEnemyFire() to explain that exactly NUMBER_SHOTS positions are required but the exception message was different.");
    }

    @Test
    void fireShots1() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> game.fireShots(List.of(new Position(0, 0))),
                "Error: expected fireShots() to reject volleys with the wrong size but no exception was thrown.");

        assertTrue(exception.getMessage().contains("Must fire exactly"),
                "Error: expected fireShots() to explain the NUMBER_SHOTS requirement but the exception message was different.");
    }

    @Test
    void fireShots2() {
        List<IPosition> shots = List.of(new Position(0, 0), new Position(0, 1), new Position(0, 2));
        game.fireShots(shots);

        assertAll(
                () -> assertEquals(1, game.getAlienMoves().size(),
                        "Error: expected fireShots() to register one alien move for a valid volley but the move count was different."),
                () -> assertEquals(1, game.getAlienMoves().get(0).getNumber(),
                        "Error: expected the first registered alien move to have number 1 but it had a different number."),
                () -> assertEquals(Game.NUMBER_SHOTS, game.getAlienMoves().get(0).getShots().size(),
                        "Error: expected the stored move to keep all fired shots but the stored size was different.")
        );
    }

    @Test
    void fireShots3() {
        IPosition repeated = new Position(0, 0);
        game.fireShots(List.of(repeated, repeated, new Position(0, 1)));

        assertAll(
                () -> assertEquals(1, game.getRepeatedShots(),
                        "Error: expected fireShots() to increment repeated shot count for duplicates within the same volley but it did not."),
                () -> assertTrue(game.getAlienMoves().get(0).getShotResults().get(1).repeated(),
                        "Error: expected the duplicated shot to be marked as repeated in the stored move results but it was not.")
        );
    }

    @Test
    void fireMyShots1() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> game.fireMyShots(List.of(new Position(0, 0))),
                "Error: expected fireMyShots() to reject volleys with the wrong size but no exception was thrown.");

        assertTrue(exception.getMessage().contains("Must fire exactly"),
                "Error: expected fireMyShots() to explain the NUMBER_SHOTS requirement but the exception message was different.");
    }

    @Test
    void fireMyShots2() {
        IFleet alienFleet = alienFleetField(game);
        List<IPosition> shots = firstWaterPositions(alienFleet, Game.NUMBER_SHOTS);
        game.fireMyShots(shots);

        assertAll(
                () -> assertEquals(1, game.getMyMoves().size(),
                        "Error: expected fireMyShots() to register one player move for a valid volley but the move count was different."),
                () -> assertEquals(1, game.getMyMoves().get(0).getNumber(),
                        "Error: expected the first stored player move to have number 1 but it had a different number."),
                () -> assertEquals(Game.NUMBER_SHOTS, game.getMyMoves().get(0).getShots().size(),
                        "Error: expected the stored player move to keep all fired shots but the stored size was different.")
        );
    }

    @Test
    void fireMyShots3() {
        IPosition repeated = new Position(0, 0);
        game.fireMyShots(List.of(repeated, repeated, new Position(0, 1)));

        assertTrue(game.getMyMoves().get(0).getShotResults().get(1).repeated(),
                "Error: expected fireMyShots() to mark duplicated shots within the same volley as repeated but it did not.");
    }

    @Test
    void fireSingleShot1() {
        IGame.ShotResult result = game.fireSingleShot(new Position(-1, 0), false);

        assertAll(
                () -> assertFalse(result.valid(),
                        "Error: expected fireSingleShot() to mark outside shots as invalid but it returned a valid result."),
                () -> assertEquals(1, game.getInvalidShots(),
                        "Error: expected fireSingleShot() to increment invalid shot count for outside shots but it did not.")
        );
    }

    @Test
    void fireSingleShot2() {
        IGame.ShotResult result = game.fireSingleShot(new Position(0, 0), true);

        assertAll(
                () -> assertTrue(result.repeated(),
                        "Error: expected fireSingleShot() to mark explicitly repeated shots as repeated but it did not."),
                () -> assertEquals(1, game.getRepeatedShots(),
                        "Error: expected fireSingleShot() to increment repeated shot count for explicitly repeated shots but it did not.")
        );
    }

    @Test
    void fireSingleShot3() {
        IPosition repeated = new Position(0, 0);
        game.fireShots(List.of(repeated, new Position(0, 1), new Position(0, 2)));

        IGame.ShotResult result = game.fireSingleShot(repeated, false);

        assertTrue(result.repeated(),
                "Error: expected fireSingleShot() to detect positions fired in previous alien moves as repeated but it did not.");
    }

    @Test
    void fireSingleShot4() {
        IGame.ShotResult result = game.fireSingleShot(firstWaterPosition(myFleet), false);

        assertAll(
                () -> assertTrue(result.valid(),
                        "Error: expected fireSingleShot() to accept an inside-water shot as valid but it did not."),
                () -> assertNull(result.ship(),
                        "Error: expected fireSingleShot() to report no ship for a water shot but it returned a ship.")
        );
    }

    @Test
    void fireSingleShot5() {
        IPosition target = myCaravel.getPositions().get(0);
        IGame.ShotResult result = game.fireSingleShot(target, false);

        assertAll(
                () -> assertSame(myCaravel, result.ship(),
                        "Error: expected fireSingleShot() to return the hit caravel but it returned a different ship."),
                () -> assertFalse(result.sunk(),
                        "Error: expected fireSingleShot() to keep the caravel afloat after a single hit but it marked it as sunk."),
                () -> assertEquals(1, game.getHits(),
                        "Error: expected fireSingleShot() to increment hit count after hitting a ship but it did not.")
        );
    }

    @Test
    void fireSingleShot6() {
        IPosition target = myBarge.getPositions().get(0);
        IGame.ShotResult result = game.fireSingleShot(target, false);

        assertAll(
                () -> assertSame(myBarge, result.ship(),
                        "Error: expected fireSingleShot() to return the sunk barge but it returned a different ship."),
                () -> assertTrue(result.sunk(),
                        "Error: expected fireSingleShot() to mark a barge as sunk after its only position is hit but it did not."),
                () -> assertEquals(1, game.getSunkShips(),
                        "Error: expected fireSingleShot() to increment sunk ship count after sinking a ship but it did not.")
        );
    }

    @Test
    void fireMySingleShot1() {
        IGame.ShotResult result = game.fireMySingleShot(new Position(-1, 0), false);

        assertFalse(result.valid(),
                "Error: expected fireMySingleShot() to mark outside shots as invalid but it returned a valid result.");
    }

    @Test
    void fireMySingleShot2() {
        IGame.ShotResult result = game.fireMySingleShot(new Position(0, 0), true);

        assertTrue(result.repeated(),
                "Error: expected fireMySingleShot() to mark explicitly repeated shots as repeated but it did not.");
    }

    @Test
    void fireMySingleShot3() {
        IPosition repeated = new Position(0, 0);
        game.getMyMoves().add(new Move(1, List.of(repeated), new ArrayList<>()));

        IGame.ShotResult result = game.fireMySingleShot(repeated, false);

        assertTrue(result.repeated(),
                "Error: expected fireMySingleShot() to detect positions fired in previous player moves as repeated but it did not.");
    }

    @Test
    void fireMySingleShot4() {
        IFleet alienFleet = alienFleetField(game);
        IGame.ShotResult result = game.fireMySingleShot(firstWaterPosition(alienFleet), false);

        assertAll(
                () -> assertTrue(result.valid(),
                        "Error: expected fireMySingleShot() to accept an inside-water shot as valid but it did not."),
                () -> assertNull(result.ship(),
                        "Error: expected fireMySingleShot() to report no ship for a water shot but it returned a ship.")
        );
    }

    @Test
    void fireMySingleShot5() {
        IFleet alienFleet = alienFleetField(game);
        IShip targetShip = firstShipWithMinimumSize(alienFleet, 2);
        IGame.ShotResult result = game.fireMySingleShot(targetShip.getPositions().get(0), false);

        assertAll(
                () -> assertSame(targetShip, result.ship(),
                        "Error: expected fireMySingleShot() to return the hit alien ship but it returned a different ship."),
                () -> assertFalse(result.sunk(),
                        "Error: expected fireMySingleShot() to keep a multi-position alien ship afloat after one hit but it marked it as sunk.")
        );
    }

    @Test
    void fireMySingleShot6() {
        IFleet alienFleet = alienFleetField(game);
        IShip targetShip = firstShipWithExactSize(alienFleet, 1);
        IGame.ShotResult result = game.fireMySingleShot(targetShip.getPositions().get(0), false);

        assertAll(
                () -> assertSame(targetShip, result.ship(),
                        "Error: expected fireMySingleShot() to return the sunk alien ship but it returned a different ship."),
                () -> assertTrue(result.sunk(),
                        "Error: expected fireMySingleShot() to mark a one-position alien ship as sunk after a hit but it did not.")
        );
    }

    @Test
    void getRepeatedShots() {
        game.fireSingleShot(new Position(0, 0), true);

        assertEquals(1, game.getRepeatedShots(),
                "Error: expected getRepeatedShots() to reflect repeated shots already counted but it returned a different value.");
    }

    @Test
    void getInvalidShots() {
        game.fireSingleShot(new Position(-1, 0), false);

        assertEquals(1, game.getInvalidShots(),
                "Error: expected getInvalidShots() to reflect invalid shots already counted but it returned a different value.");
    }

    @Test
    void getHits() {
        game.fireSingleShot(myCaravel.getPositions().get(0), false);

        assertEquals(1, game.getHits(),
                "Error: expected getHits() to reflect successful hits already counted but it returned a different value.");
    }

    @Test
    void getSunkShips() {
        game.fireSingleShot(myBarge.getPositions().get(0), false);

        assertEquals(1, game.getSunkShips(),
                "Error: expected getSunkShips() to reflect sunk ships already counted but it returned a different value.");
    }

    @Test
    void getRemainingShips() {
        game.fireSingleShot(myBarge.getPositions().get(0), false);

        assertEquals(1, game.getRemainingShips(),
                "Error: expected getRemainingShips() to drop after sinking one of two ships but it returned a different value.");
    }

    @Test
    void getRemainingAlienShips() {
        IFleet alienFleet = alienFleetField(game);

        assertEquals(alienFleet.getFloatingShips().size(), game.getRemainingAlienShips(),
                "Error: expected getRemainingAlienShips() to match the reflected alien fleet floating ship count but it returned a different value.");
    }

    @Test
    void repeatedShot1() {
        IPosition fired = new Position(0, 0);
        game.fireShots(List.of(fired, new Position(0, 1), new Position(0, 2)));

        assertTrue(game.repeatedShot(fired),
                "Error: expected repeatedShot() to return true for a position already fired by the alien but it returned false.");
    }

    @Test
    void repeatedShot2() {
        assertFalse(game.repeatedShot(new Position(0, 0)),
                "Error: expected repeatedShot() to return false for a never-fired position but it returned true.");
    }

    @Test
    void myRepeatedShot1() {
        IPosition fired = new Position(0, 0);
        game.getMyMoves().add(new Move(1, List.of(new Position(5, 5)), new ArrayList<>()));
        game.getMyMoves().add(new Move(2, List.of(fired), new ArrayList<>()));

        assertTrue(game.myRepeatedShot(fired),
                "Error: expected myRepeatedShot() to return true after skipping unrelated player moves and finding the target position later, but it returned false.");
    }

    @Test
    void myRepeatedShot2() {
        game.getMyMoves().add(new Move(1, List.of(new Position(5, 5)), new ArrayList<>()));

        assertFalse(game.myRepeatedShot(new Position(0, 0)),
                "Error: expected myRepeatedShot() to return false when player moves exist but none contain the target position, and it returned true.");
    }

    @Test
    void printMyBoard() {
        String output = captureOutput(() -> game.printMyBoard(false, false));

        assertTrue(output.contains("A |"),
                "Error: expected printMyBoard() to print the board rows but the row label output was missing.");
    }

    @Test
    void printAlienBoard() {
        String output = captureOutput(() -> game.printAlienBoard(false, false));

        assertFalse(output.contains(BoardColor.ship()),
                "Error: expected printAlienBoard() to hide floating alien ships by default but a ship marker was printed.");
    }

    @Test
    void over() {
        String output = captureOutput(() -> game.over());

        assertTrue(output.contains("Game over"),
                "Error: expected over() to print the localized game-over message but the output was different.");
    }

    private IFleet alienFleetField(Game target) {
        try {
            Field field = Game.class.getDeclaredField("alienFleet");
            field.setAccessible(true);
            return (IFleet) field.get(target);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Error: expected reflection access to alienFleet to succeed but it failed.", e);
        }
    }

    private IFleet mockFleetWithNoShips() {
        IFleet fleet = mock(IFleet.class);
        when(fleet.getSunkShips()).thenReturn(List.of());
        when(fleet.getShips()).thenReturn(List.of());
        when(fleet.shipAt(any())).thenReturn(null);
        when(fleet.getFloatingShips()).thenReturn(List.of());
        return fleet;
    }

    private IPosition firstWaterPosition(IFleet fleet) {
        for (int row = 0; row < Game.BOARD_SIZE; row++) {
            for (int column = 0; column < Game.BOARD_SIZE; column++) {
                IPosition candidate = new Position(row, column);
                if (fleet.shipAt(candidate) == null) {
                    return candidate;
                }
            }
        }
        throw new AssertionError("Error: expected to find at least one water position on the board but none was found.");
    }

    private List<IPosition> firstWaterPositions(IFleet fleet, int count) {
        List<IPosition> positions = new ArrayList<>();
        for (int row = 0; row < Game.BOARD_SIZE && positions.size() < count; row++) {
            for (int column = 0; column < Game.BOARD_SIZE && positions.size() < count; column++) {
                IPosition candidate = new Position(row, column);
                if (fleet.shipAt(candidate) == null) {
                    positions.add(candidate);
                }
            }
        }
        if (positions.size() != count) {
            throw new AssertionError(
                    "Error: expected to find " + count + " distinct water positions but found a different amount.");
        }
        return positions;
    }

    private IShip firstShipWithMinimumSize(IFleet fleet, int size) {
        return fleet.getShips().stream()
                .filter(ship -> ship.getSize() >= size)
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "Error: expected to find an alien ship with size >= " + size + " but none was found."));
    }

    private IShip firstShipWithExactSize(IFleet fleet, int size) {
        return fleet.getShips().stream()
                .filter(ship -> ship.getSize() == size)
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "Error: expected to find an alien ship with size == " + size + " but none was found."));
    }

    private List<IPosition> boardPositionsExcept(Set<IPosition> allowedPositions) {
        List<IPosition> blocked = new ArrayList<>();
        for (int row = 0; row < Game.BOARD_SIZE; row++) {
            for (int column = 0; column < Game.BOARD_SIZE; column++) {
                IPosition candidate = new Position(row, column);
                if (!allowedPositions.contains(candidate)) {
                    blocked.add(candidate);
                }
            }
        }
        return blocked;
    }

    private Map<String, Integer> shotOccurrences(List<Map<String, Object>> parsedShots) {
        Map<String, Integer> occurrences = new java.util.HashMap<>();
        for (Map<String, Object> shot : parsedShots) {
            String key = shot.get("row") + ":" + shot.get("column");
            occurrences.merge(key, 1, Integer::sum);
        }
        return occurrences;
    }

    private String captureOutput(Runnable action) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try (PrintStream capture = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            action.run();
        } finally {
            System.setOut(originalOut);
        }

        return buffer.toString(StandardCharsets.UTF_8);
    }

    private List<Map<String, Object>> parseJsonArray(String json) throws Exception {
        return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
        });
    }
}
