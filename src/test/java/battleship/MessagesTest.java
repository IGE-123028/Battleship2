package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessagesTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void load1() {
        assertDoesNotThrow(() -> Messages.load("pt"));
    }

    @Test
    void load2() {
        assertDoesNotThrow(() -> Messages.load("en"));
    }

    @Test
    void load() throws Exception {
        Messages.load("en");
        assertEquals("Game finished", Messages.get("game_finished"));
    }

    @Test
    void get() {
        assertNotNull(Messages.get("game_finished"));
    }

    @Test
    void get1() {
        String msg = Messages.get("game_finished");
        assertFalse(msg.isEmpty());
    }
}
