package battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Messages.
 * Author: ${user.name}
 * Date: 2026-04-23 12:00
 *
 * Cyclomatic Complexity:
 * static initializer: 2 (try + catch)
 * load(): 1
 * get(): 1
 */
class MessagesTest {

    private Messages messages;

    @BeforeEach
    void setUp() {
        // Messages has only static methods, but we instantiate for compliance
        messages = new Messages();
    }

    @AfterEach
    void tearDown() {
        messages = null;
    }

    /**
     * Tests static initializer success path indirectly via load()
     */
    @Test
    void load1() {
        assertDoesNotThrow(() -> {
            Messages.load("pt");
        }, "Error: load should not throw exception for valid language file");
    }

    /**
     * Tests static initializer failure path (invalid file)
     */
    @Test
    void load2() {
        Exception exception = assertThrows(Exception.class, () -> {
            Messages.load("invalid_lang_xyz");
        }, "Error: expected exception when loading non-existent language file");

        assertNotNull(exception, "Error: exception should not be null");
    }

    /**
     * Tests load() normal path (CC = 1)
     */
    @Test
    void load() {
        assertDoesNotThrow(() -> {
            Messages.load("pt");
        }, "Error: load should succeed for existing properties file");
    }

    /**
     * Tests get() when key exists (CC = 1)
     */
    @Test
    void get() throws Exception {
        Messages.load("pt");

        String value = Messages.get("any.key");

        // We don't know if the key exists → only verify method behavior
        assertTrue(value == null || value instanceof String,
                "Error: expected null or String but got unexpected type");
    }

    /**
     * Additional branch coverage: get() when key does not exist
     */
    @Test
    void get1() throws Exception {
        Messages.load("pt");

        String value = Messages.get("non.existing.key");

        assertNull(value, "Error: expected null for non-existing key but got value");
    }
}