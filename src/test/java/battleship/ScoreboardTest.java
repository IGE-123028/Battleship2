package battleship;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreboardTest {

    private String filePath;

    @BeforeEach
    void setUp() {
        String homeDir = System.getProperty("user.home");
        filePath = homeDir + File.separator + "battleship_scoreboard.csv";

        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            fail("Error: could not clean test file before execution");
        }
    }

    @AfterEach
    void tearDown() {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            fail("Error: could not clean test file after execution");
        }
    }

    @Test
    void saveResult1() throws IOException {
        Scoreboard.saveResult("WIN");

        File file = new File(filePath);

        assertAll(
                () -> assertTrue(file.exists(), "Error: expected file to be created"),
                () -> assertTrue(file.length() > 0, "Error: expected file not to be empty")
        );

        List<String> lines = Files.readAllLines(file.toPath());

        assertEquals(1, lines.size(), "Error: expected 1 line");
        assertTrue(lines.get(0).contains("WIN"), "Error: expected line to contain WIN");
    }

    @Test
    void saveResult2() throws IOException {
        Scoreboard.saveResult("WIN");
        Scoreboard.saveResult("LOSS");

        List<String> lines = Files.readAllLines(Paths.get(filePath));

        assertAll(
                () -> assertEquals(2, lines.size(), "Error: expected 2 lines"),
                () -> assertTrue(lines.get(0).contains("WIN"), "Error: first line should contain WIN"),
                () -> assertTrue(lines.get(1).contains("LOSS"), "Error: second line should contain LOSS")
        );
    }

    @Test
    void saveResult3() throws IOException {
        Scoreboard.saveResult(null);

        List<String> lines = Files.readAllLines(Paths.get(filePath));

        assertEquals(1, lines.size(), "Error: expected 1 line");
        assertTrue(lines.get(0).endsWith(","),
                "Error: expected empty result field for null input");
    }
}