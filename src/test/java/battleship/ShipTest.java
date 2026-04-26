package battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShipTest {

    private Ship ship;

    @BeforeEach
    void setUp() {
        ship = new Barge(Compass.NORTH, new Position(5, 5));
    }

    @Test
    @DisplayName("Should create a barge with correct initial values")
    void testConstructor() {
        assertNotNull(ship, "Ship instance should not be null.");
        assertEquals("Barca", ship.getCategory(), "Ship category is incorrect.");
        assertEquals(Compass.NORTH, ship.getBearing(), "Ship bearing is incorrect.");
        assertEquals(1, ship.getSize(), "Ship size is incorrect.");
        assertFalse(ship.getPositions().isEmpty(), "Ship positions should not be empty.");
    }

    @Test
    @DisplayName("Should return Barca as category")
    void testGetCategory() {
        assertEquals("Barca", ship.getCategory(), "Ship category should be 'Barca'.");
    }

    @Test
    @DisplayName("Should return size 1 for a barge")
    void testGetSize() {
        assertEquals(1, ship.getSize(), "Ship size should be 1.");
    }

    @Test
    @DisplayName("Should return NORTH as bearing")
    void testGetBearing() {
        assertEquals(Compass.NORTH, ship.getBearing(), "Ship bearing should be NORTH.");
    }

    @Test
    @DisplayName("Should return the initial ship position")
    void testGetPosition() {
        assertEquals(new Position(5, 5), ship.getPosition());
    }

    @Test
    @DisplayName("Should return the occupied positions of the ship")
    void testGetPositions() {
        List<IPosition> positions = ship.getPositions();

        assertNotNull(positions, "Ship positions should not be null.");
        assertEquals(1, positions.size(), "Ship should have exactly one position.");
        assertEquals(5, positions.get(0).getRow(), "Position row should be 5.");
        assertEquals(5, positions.get(0).getColumn(), "Position column should be 5.");
    }

    @Test
    @DisplayName("Should return all adjacent positions around a barge")
    void testGetAdjacentPositions() {
        List<IPosition> adjacent = ship.getAdjacentPositions();

        assertNotNull(adjacent);
        assertEquals(8, adjacent.size());

        assertTrue(adjacent.contains(new Position(4, 4)));
        assertTrue(adjacent.contains(new Position(4, 5)));
        assertTrue(adjacent.contains(new Position(4, 6)));
        assertTrue(adjacent.contains(new Position(5, 4)));
        assertTrue(adjacent.contains(new Position(5, 6)));
        assertTrue(adjacent.contains(new Position(6, 4)));
        assertTrue(adjacent.contains(new Position(6, 5)));
        assertTrue(adjacent.contains(new Position(6, 6)));
    }

    @Test
    @DisplayName("Should still be floating when not hit")
    void testStillFloating1() {
        assertTrue(ship.stillFloating(), "Ship should still be floating.");
    }

    @Test
    @DisplayName("Should no longer be floating after being hit")
    void testStillFloating2() {
        ship.getPositions().get(0).shoot();
        assertFalse(ship.stillFloating(), "Ship should no longer be floating after being hit.");
    }

    @Test
    @DisplayName("Should mark position as hit when shooting an occupied position")
    void testShoot1() {
        Position target = new Position(5, 5);
        ship.shoot(target);
        assertTrue(ship.getPositions().get(0).isHit(), "Position should be marked as hit.");
    }

    @Test
    @DisplayName("Should occupy position 5 5")
    void testOccupies1() {
        Position pos = new Position(5, 5);
        assertTrue(ship.occupies(pos), "Ship should occupy position (5, 5).");
    }

    @Test
    @DisplayName("Should not occupy a different position")
    void testOccupies2() {
        Position pos = new Position(1, 1);
        assertFalse(ship.occupies(pos), "Ship should not occupy position (1, 1).");
    }

    @Test
    @DisplayName("Should detect when another ship is too close")
    void testTooCloseToShip1() {
        Ship nearbyShip = new Barge(Compass.NORTH, new Position(5, 6));
        assertTrue(ship.tooCloseTo(nearbyShip), "Ships should be too close.");
    }

    @Test
    @DisplayName("Should detect when another ship is not too close")
    void testTooCloseToShip2() {
        Ship farShip = new Barge(Compass.NORTH, new Position(10, 10));
        assertFalse(ship.tooCloseTo(farShip), "Ships should not be too close.");
    }

    @Test
    @DisplayName("Should detect when a position is adjacent to the ship")
    void testTooCloseToPosition1() {
        Position pos = new Position(5, 6);
        assertTrue(ship.tooCloseTo(pos), "Ship should be too close to the given position.");
    }

    @Test
    @DisplayName("Should detect when a position is not adjacent to the ship")
    void testTooCloseToPosition2() {
        Position pos = new Position(7, 7);
        assertFalse(ship.tooCloseTo(pos), "Ship should not be too close to the given position.");
    }

    @Test
    @DisplayName("Should return the topmost row of the ship")
    void testGetTopMostPos() {
        assertEquals(5, ship.getTopMostPos(), "The topmost position should be 5.");
    }

    @Test
    @DisplayName("Should return the bottommost row of the ship")
    void testGetBottomMostPos() {
        assertEquals(5, ship.getBottomMostPos(), "The bottommost position should be 5.");
    }

    @Test
    @DisplayName("Should return the leftmost column of the ship")
    void testGetLeftMostPos() {
        assertEquals(5, ship.getLeftMostPos(), "The leftmost position should be 5.");
    }

    @Test
    @DisplayName("Should return the rightmost column of the ship")
    void testGetRightMostPos() {
        assertEquals(5, ship.getRightMostPos(), "The rightmost position should be 5.");
    }

    @Test
    @DisplayName("Should sink the ship by marking all positions as hit")
    void testSink() {
        ship.sink();

        for (IPosition position : ship.getPositions()) {
            assertTrue(position.isHit());
        }

        assertFalse(ship.stillFloating());
    }

    @Test
    @DisplayName("Should return a string representation of the ship")
    void testToString() {
        String result = ship.toString();

        assertNotNull(result);
        assertTrue(result.startsWith("["));
        assertTrue(result.endsWith("]"));

        assertTrue(result.toLowerCase().contains("barca"));

        // validar pela string real do bearing, sem assumir formato fixo
        assertTrue(result.contains(ship.getBearing().toString()));

        // validar também que inclui a posição
        assertTrue(result.contains(ship.getPosition().toString()));
    }

    @Test
    @DisplayName("Should build a Barge when kind is barca")
    void testBuildShipBarca() {
        Ship built = Ship.buildShip("barca", Compass.NORTH, new Position(1, 1));
        assertNotNull(built);
        assertInstanceOf(Barge.class, built);
    }

    @Test
    @DisplayName("Should build a Caravel when kind is caravela")
    void testBuildShipCaravela() {
        Ship built = Ship.buildShip("caravela", Compass.NORTH, new Position(1, 1));
        assertNotNull(built);
        assertInstanceOf(Caravel.class, built);
    }

    @Test
    @DisplayName("Should build a Carrack when kind is nau")
    void testBuildShipNau() {
        Ship built = Ship.buildShip("nau", Compass.NORTH, new Position(1, 1));
        assertNotNull(built);
        assertInstanceOf(Carrack.class, built);
    }

    @Test
    @DisplayName("Should build a Frigate when kind is fragata")
    void testBuildShipFragata() {
        Ship built = Ship.buildShip("fragata", Compass.NORTH, new Position(1, 1));
        assertNotNull(built);
        assertInstanceOf(Frigate.class, built);
    }

    @Test
    @DisplayName("Should build a Galleon when kind is galeao")
    void testBuildShipGaleao() {
        Ship built = Ship.buildShip("galeao", Compass.NORTH, new Position(3, 3));
        assertNotNull(built);
        assertInstanceOf(Galleon.class, built);
    }

    @Test
    @DisplayName("Should return null for an unknown ship kind")
    void testBuildShipInvalidKind() {
        Ship built = Ship.buildShip("unknown", Compass.NORTH, new Position(1, 1));
        assertNull(built);
    }
}