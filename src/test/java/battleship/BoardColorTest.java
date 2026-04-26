package battleship;

import com.diogonunes.jcolor.AnsiFormat;
import com.diogonunes.jcolor.Attribute;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test class for class BoardColor.
 * Author: manas
 * Date: 2026-04-23 16:53
 * Cyclomatic Complexity:
 * - constructor: 1
 * - water(): 1
 * - ship(): 1
 * - hit(): 1
 * - miss(): 1
 * - adjacent(): 1
 * - colored(char): 6
 */
class BoardColorTest {

    private static final AnsiFormat WATER = new AnsiFormat(Attribute.BLUE_TEXT());
    private static final AnsiFormat SHIP = new AnsiFormat(Attribute.GREEN_TEXT());
    private static final AnsiFormat HIT = new AnsiFormat(Attribute.RED_TEXT());
    private static final AnsiFormat MISS = new AnsiFormat(Attribute.CYAN_TEXT());
    private static final AnsiFormat ADJACENT = new AnsiFormat(Attribute.YELLOW_TEXT());

    private BoardColor boardColor;

    @BeforeEach
    void setUp() {
        boardColor = new BoardColor();
    }

    @AfterEach
    void tearDown() {
        boardColor = null;
        assertNull(boardColor, "Error: expected boardColor to be null after teardown but it was not null.");
    }

    @Test
    void constructor() {
        assertNotNull(boardColor, "Error: expected BoardColor instance to be created in setup but it was null.");
    }

    @Test
    void water() {
        assertAll(
                () -> assertNotNull(boardColor, "Error: expected BoardColor test fixture to exist but it was null."),
                () -> assertEquals(WATER.format("."), BoardColor.water(),
                        "Error: expected water() to return the blue formatted '.' marker but got a different value.")
        );
    }

    @Test
    void ship() {
        assertAll(
                () -> assertNotNull(boardColor, "Error: expected BoardColor test fixture to exist but it was null."),
                () -> assertEquals(SHIP.format("#"), BoardColor.ship(),
                        "Error: expected ship() to return the green formatted '#' marker but got a different value.")
        );
    }

    @Test
    void hit() {
        assertAll(
                () -> assertNotNull(boardColor, "Error: expected BoardColor test fixture to exist but it was null."),
                () -> assertEquals(HIT.format("*"), BoardColor.hit(),
                        "Error: expected hit() to return the red formatted '*' marker but got a different value.")
        );
    }

    @Test
    void miss() {
        assertAll(
                () -> assertNotNull(boardColor, "Error: expected BoardColor test fixture to exist but it was null."),
                () -> assertEquals(MISS.format("o"), BoardColor.miss(),
                        "Error: expected miss() to return the cyan formatted 'o' marker but got a different value.")
        );
    }

    @Test
    void adjacent() {
        assertAll(
                () -> assertNotNull(boardColor, "Error: expected BoardColor test fixture to exist but it was null."),
                () -> assertEquals(ADJACENT.format("-"), BoardColor.adjacent(),
                        "Error: expected adjacent() to return the yellow formatted '-' marker but got a different value.")
        );
    }

    @Test
    void colored1() {
        assertAll(
                () -> assertNotNull(boardColor, "Error: expected BoardColor test fixture to exist but it was null."),
                () -> assertEquals(BoardColor.ship(), BoardColor.colored('#'),
                        "Error: expected colored('#') to delegate to ship() but it returned a different value.")
        );
    }

    @Test
    void colored2() {
        assertAll(
                () -> assertNotNull(boardColor, "Error: expected BoardColor test fixture to exist but it was null."),
                () -> assertEquals(BoardColor.hit(), BoardColor.colored('*'),
                        "Error: expected colored('*') to delegate to hit() but it returned a different value.")
        );
    }

    @Test
    void colored3() {
        assertAll(
                () -> assertNotNull(boardColor, "Error: expected BoardColor test fixture to exist but it was null."),
                () -> assertEquals(BoardColor.miss(), BoardColor.colored('o'),
                        "Error: expected colored('o') to delegate to miss() but it returned a different value.")
        );
    }

    @Test
    void colored4() {
        assertAll(
                () -> assertNotNull(boardColor, "Error: expected BoardColor test fixture to exist but it was null."),
                () -> assertEquals(BoardColor.adjacent(), BoardColor.colored('-'),
                        "Error: expected colored('-') to delegate to adjacent() but it returned a different value.")
        );
    }

    @Test
    void colored5() {
        assertAll(
                () -> assertNotNull(boardColor, "Error: expected BoardColor test fixture to exist but it was null."),
                () -> assertEquals(BoardColor.water(), BoardColor.colored('.'),
                        "Error: expected colored('.') to delegate to water() but it returned a different value.")
        );
    }

    @Test
    void colored6() {
        assertAll(
                () -> assertNotNull(boardColor, "Error: expected BoardColor test fixture to exist but it was null."),
                () -> assertEquals(String.valueOf('X'), BoardColor.colored('X'),
                        "Error: expected colored('X') to return the original marker string but it returned a different value.")
        );
    }
}
