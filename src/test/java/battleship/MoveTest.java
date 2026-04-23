package battleship;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

    @Test
    @DisplayName("Should return a string representation of the move")
    void testToString() {
        Move move = new Move(7, new ArrayList<>(), new ArrayList<>());

        String result = move.toString();

        assertNotNull(result);
        assertTrue(result.contains("number=7"));
        assertTrue(result.contains("shots=0"));
        assertTrue(result.contains("results=0"));
    }

    @Test
    @DisplayName("Should return the move number")
    void getNumber() {
        Move move = new Move(3, new ArrayList<>(), new ArrayList<>());
        assertEquals(3, move.getNumber());
    }

    @Test
    @DisplayName("Should return the shots list")
    void getShots() {
        List<IPosition> shots = new ArrayList<>();
        shots.add(new Position(1, 1));
        shots.add(new Position(2, 2));

        Move move = new Move(1, shots, new ArrayList<>());

        assertEquals(shots, move.getShots());
        assertEquals(2, move.getShots().size());
    }

    @Test
    @DisplayName("Should return the shot results list")
    void getShotResults() throws Exception {
        List<IGame.ShotResult> results = new ArrayList<>();
        results.add(newShotResult(true, false, null, false));

        Move move = new Move(1, new ArrayList<>(), results);

        assertEquals(results, move.getShotResults());
        assertEquals(1, move.getShotResults().size());
    }

    @Test
    @DisplayName("Should process enemy fire with miss hit and repeated shot")
    void processEnemyFire() throws Exception {
        List<IPosition> shots = new ArrayList<>();
        shots.add(new Position(1, 1));
        shots.add(new Position(2, 2));
        shots.add(new Position(3, 3));

        IShip ship = new Barge(Compass.NORTH, new Position(2, 2));

        List<IGame.ShotResult> results = new ArrayList<>();
        results.add(newShotResult(true, false, null, false));   // miss
        results.add(newShotResult(true, false, ship, false));   // hit
        results.add(newShotResult(true, true, null, false));    // repeated

        Move move = new Move(1, shots, results);

        String json = move.processEnemyFire(false);

        assertNotNull(json);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        assertEquals(2, root.get("validShots").asInt());
        assertEquals(0, root.get("outsideShots").asInt());
        assertEquals(1, root.get("repeatedShots").asInt());
        assertEquals(1, root.get("missedShots").asInt());

        JsonNode sunkBoats = root.get("sunkBoats");
        assertTrue(sunkBoats.isArray());
        assertEquals(0, sunkBoats.size());

        JsonNode hitsOnBoats = root.get("hitsOnBoats");
        assertTrue(hitsOnBoats.isArray());
        assertEquals(1, hitsOnBoats.size());
        assertEquals("Barca", hitsOnBoats.get(0).get("type").asText());
        assertEquals(1, hitsOnBoats.get(0).get("hits").asInt());
    }

    @Test
    @DisplayName("Should process enemy fire with sunk ship and outside shots")
    void processEnemyFireWithSunkAndOutsideShots() throws Exception {
        List<IPosition> shots = new ArrayList<>();
        shots.add(new Position(1, 1));
        shots.add(new Position(2, 2));
        shots.add(new Position(3, 3));

        IShip ship = new Barge(Compass.NORTH, new Position(2, 2));

        List<IGame.ShotResult> results = new ArrayList<>();
        results.add(newShotResult(true, false, ship, true));    // sunk
        results.add(newShotResult(false, false, null, false));  // outside
        results.add(newShotResult(false, false, null, false));  // outside

        Move move = new Move(2, shots, results);

        String json = move.processEnemyFire(true);

        assertNotNull(json);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        assertEquals(1, root.get("validShots").asInt());
        assertEquals(2, root.get("outsideShots").asInt());
        assertEquals(0, root.get("repeatedShots").asInt());
        assertEquals(0, root.get("missedShots").asInt());

        JsonNode sunkBoats = root.get("sunkBoats");
        assertTrue(sunkBoats.isArray());
        assertEquals(1, sunkBoats.size());
        assertEquals("Barca", sunkBoats.get(0).get("type").asText());
        assertEquals(1, sunkBoats.get(0).get("count").asInt());

        JsonNode hitsOnBoats = root.get("hitsOnBoats");
        assertTrue(hitsOnBoats.isArray());
        assertEquals(0, hitsOnBoats.size());
    }

    /**
     * Creates an instance of IGame.ShotResult using reflection, so the test works
     * even if ShotResult is implemented as a record or a normal nested class.
     */
    @SuppressWarnings("unchecked")
    private IGame.ShotResult newShotResult(boolean valid, boolean repeated, IShip ship, boolean sunk) throws Exception {
        Class<?> shotResultClass = null;

        for (Class<?> nested : IGame.class.getDeclaredClasses()) {
            if (nested.getSimpleName().equals("ShotResult")) {
                shotResultClass = nested;
                break;
            }
        }

        assertNotNull(shotResultClass, "Could not find IGame.ShotResult nested type.");

        // Case 1: ShotResult is a record
        if (shotResultClass.isRecord()) {
            RecordComponent[] components = shotResultClass.getRecordComponents();
            Class<?>[] parameterTypes = new Class<?>[components.length];
            Object[] args = new Object[components.length];

            for (int i = 0; i < components.length; i++) {
                String name = components[i].getName();
                parameterTypes[i] = components[i].getType();

                switch (name) {
                    case "valid" -> args[i] = valid;
                    case "repeated" -> args[i] = repeated;
                    case "ship" -> args[i] = ship;
                    case "sunk" -> args[i] = sunk;
                    default -> args[i] = defaultValue(components[i].getType());
                }
            }

            Constructor<?> constructor = shotResultClass.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return (IGame.ShotResult) constructor.newInstance(args);
        }

        // Case 2: normal class - try to guess the constructor
        for (Constructor<?> constructor : shotResultClass.getDeclaredConstructors()) {
            constructor.setAccessible(true);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> type = parameterTypes[i];

                if (type == boolean.class || type == Boolean.class) {
                    // first boolean -> valid, second -> repeated, third -> sunk
                    long booleanCountBefore = countBooleansBefore(parameterTypes, i);
                    if (booleanCountBefore == 0) args[i] = valid;
                    else if (booleanCountBefore == 1) args[i] = repeated;
                    else args[i] = sunk;
                } else if (IShip.class.isAssignableFrom(type)) {
                    args[i] = ship;
                } else {
                    args[i] = defaultValue(type);
                }
            }

            try {
                return (IGame.ShotResult) constructor.newInstance(args);
            } catch (Exception ignored) {
                // try next constructor
            }
        }

        fail("Could not instantiate IGame.ShotResult.");
        return null;
    }

    private long countBooleansBefore(Class<?>[] parameterTypes, int index) {
        long count = 0;
        for (int i = 0; i < index; i++) {
            if (parameterTypes[i] == boolean.class || parameterTypes[i] == Boolean.class) {
                count++;
            }
        }
        return count;
    }

    private Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) return null;
        if (type == boolean.class) return false;
        if (type == byte.class) return (byte) 0;
        if (type == short.class) return (short) 0;
        if (type == int.class) return 0;
        if (type == long.class) return 0L;
        if (type == float.class) return 0f;
        if (type == double.class) return 0d;
        if (type == char.class) return '\0';
        return null;
    }
}