package battleship;

import com.diogonunes.jcolor.AnsiFormat;
import com.diogonunes.jcolor.Attribute;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardColorTest {

    private static final AnsiFormat WATER = new AnsiFormat(Attribute.BLUE_TEXT());
    private static final AnsiFormat SHIP = new AnsiFormat(Attribute.GREEN_TEXT());
    private static final AnsiFormat HIT = new AnsiFormat(Attribute.RED_TEXT());
    private static final AnsiFormat MISS = new AnsiFormat(Attribute.CYAN_TEXT());
    private static final AnsiFormat ADJACENT = new AnsiFormat(Attribute.YELLOW_TEXT());

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void water() {
        assertEquals(WATER.format("."), BoardColor.water(),
                "Error: expected water() to return the blue formatted '.' marker but got a different value.");
    }

    @Test
    void ship() {
        assertEquals(SHIP.format("#"), BoardColor.ship(),
                "Error: expected ship() to return the green formatted '#' marker but got a different value.");
    }

    @Test
    void hit() {
        assertEquals(HIT.format("*"), BoardColor.hit(),
                "Error: expected hit() to return the red formatted '*' marker but got a different value.");
    }

    @Test
    void miss() {
        assertEquals(MISS.format("o"), BoardColor.miss(),
                "Error: expected miss() to return the cyan formatted 'o' marker but got a different value.");
    }

    @Test
    void adjacent() {
        assertEquals(ADJACENT.format("-"), BoardColor.adjacent(),
                "Error: expected adjacent() to return the yellow formatted '-' marker but got a different value.");
    }

}
