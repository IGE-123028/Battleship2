package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FleetTest {

    private Fleet fleet;

    @BeforeEach
    void setUp() {
        fleet = new Fleet();
    }

    @AfterEach
    void tearDown() {
        fleet = null;
    }

    @Test
    @DisplayName("Should create an empty fleet")
    void testConstructor() {
        assertNotNull(fleet, "Instance of Fleet should not be null.");
        assertTrue(fleet.getShips().isEmpty(), "Fleet should be initialized with an empty ships list.");
    }

    @Test
    @DisplayName("Should add a valid ship successfully")
    void testAddShip1() {
        IShip ship = new Barge(Compass.NORTH, new Position(1, 1));

        assertTrue(fleet.addShip(ship), "Valid ship should be added successfully.");
        assertEquals(1, fleet.getShips().size(), "Fleet should contain one ship after addition.");
    }

    @Test
    @DisplayName("Should not add ship when fleet size limit is reached")
    void testAddShip2() {
        fleet.addShip(new Barge(Compass.NORTH, new Position(0, 0)));
        fleet.addShip(new Barge(Compass.NORTH, new Position(0, 2)));
        fleet.addShip(new Barge(Compass.NORTH, new Position(0, 4)));
        fleet.addShip(new Barge(Compass.NORTH, new Position(0, 6)));
        fleet.addShip(new Barge(Compass.NORTH, new Position(0, 8)));

        fleet.addShip(new Barge(Compass.NORTH, new Position(2, 0)));
        fleet.addShip(new Barge(Compass.NORTH, new Position(2, 2)));
        fleet.addShip(new Barge(Compass.NORTH, new Position(2, 4)));
        fleet.addShip(new Barge(Compass.NORTH, new Position(2, 6)));
        fleet.addShip(new Barge(Compass.NORTH, new Position(2, 8)));

        fleet.addShip(new Barge(Compass.NORTH, new Position(4, 0)));

        assertEquals(Fleet.FLEET_SIZE, fleet.getShips().size());

        IShip anotherShip = new Barge(Compass.NORTH, new Position(4, 2));
            assertFalse(fleet.addShip(anotherShip),
                "Should not add ship when fleet size limit is reached.");
    }

    @Test
    @DisplayName("Should not add a ship outside the board")
    void testAddShip3() {
        IShip shipOutside = new Barge(Compass.NORTH, new Position(99, 99));

        assertFalse(fleet.addShip(shipOutside), "Should not add ship outside the board.");
    }

    @Test
    @DisplayName("Should not add a ship with collision risk")
    void testAddShip4() {
        IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
        IShip ship2 = new Barge(Compass.NORTH, new Position(1, 1));

        fleet.addShip(ship1);

        assertFalse(fleet.addShip(ship2), "Should not add ship with a collision risk.");
    }

    @Test
    @DisplayName("Should return all ships in the fleet")
    void testGetShips() {
        assertTrue(fleet.getShips().isEmpty(), "Fleet ships list should initially be empty.");

        IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
        fleet.addShip(ship);

        assertEquals(1, fleet.getShips().size(), "Fleet should have size 1 after adding a ship.");
        assertEquals(ship, fleet.getShips().get(0), "Fleet first ship should match the added ship.");
    }

    @Test
    @DisplayName("Should return ships of a given category")
    void testGetShipsLike() {
        IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
        IShip ship2 = new Caravel(Compass.NORTH, new Position(4, 4));

        fleet.addShip(ship1);
        fleet.addShip(ship2);

        List<IShip> barges = fleet.getShipsLike("Barca");
        assertEquals(1, barges.size(), "There should be exactly one ship of category Barca.");
        assertEquals(ship1, barges.get(0), "The returned ship should be the barge.");
    }

    @Test
    @DisplayName("Should return empty list when category does not exist")
    void testGetShipsLikeNoMatch() {
        fleet.addShip(new Barge(Compass.NORTH, new Position(1, 1)));

        List<IShip> frigates = fleet.getShipsLike("Fragata");
        assertNotNull(frigates);
        assertTrue(frigates.isEmpty());
    }

    @Test
    @DisplayName("Should return floating ships only")
    void testGetFloatingShips() {
        IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
        IShip ship2 = new Caravel(Compass.NORTH, new Position(4, 4));

        fleet.addShip(ship1);
        fleet.addShip(ship2);

        List<IShip> floatingShips = fleet.getFloatingShips();
        assertEquals(2, floatingShips.size(), "All ships should be floating initially.");

        ship1.getPositions().get(0).shoot();

        floatingShips = fleet.getFloatingShips();
        assertEquals(1, floatingShips.size(), "Only one ship should be floating after sinking one.");
        assertEquals(ship2, floatingShips.get(0), "The floating ship should be the caravel.");
    }

    @Test
    @DisplayName("Should return sunk ships only")
    void testGetSunkShips() {
        IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
        IShip ship2 = new Barge(Compass.NORTH, new Position(3, 3));

        fleet.addShip(ship1);
        fleet.addShip(ship2);

        assertEquals(0, fleet.getSunkShips().size());

        ship1.sink();

        List<IShip> sunkShips = fleet.getSunkShips();
        assertEquals(1, sunkShips.size());
        assertEquals(ship1, sunkShips.get(0));
    }

    @Test
    @DisplayName("Should return the ship at a given position")
    void testShipAt() {
        IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
        fleet.addShip(ship);

        assertEquals(ship, fleet.shipAt(new Position(1, 1)),
                "Should return the correct ship at the position.");
        assertNull(fleet.shipAt(new Position(5, 5)),
                "Should return null for an empty position.");
    }

    @Test
    @DisplayName("Should detect if a ship is inside the board")
    void testIsInsideBoard() throws Exception {
        var method = Fleet.class.getDeclaredMethod("isInsideBoard", IShip.class);
        method.setAccessible(true);

        IShip insideShip = new Barge(Compass.NORTH, new Position(1, 1));
        IShip outsideShip = new Barge(Compass.NORTH, new Position(99, 99));

        assertTrue((Boolean) method.invoke(fleet, insideShip),
                "Ship inside the board should return true.");
        assertFalse((Boolean) method.invoke(fleet, outsideShip),
                "Ship outside the board should return false.");
    }

    @Test
    @DisplayName("Should detect collision risk correctly")
    void testColisionRisk() throws Exception {
        var method = Fleet.class.getDeclaredMethod("colisionRisk", IShip.class);
        method.setAccessible(true);

        IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
        IShip ship2 = new Barge(Compass.NORTH, new Position(1, 1));

        fleet.addShip(ship1);

        assertTrue((Boolean) method.invoke(fleet, ship2),
                "Overlapping ships should have collision risk.");
        assertFalse((Boolean) method.invoke(fleet, new Barge(Compass.NORTH, new Position(5, 5))),
                "Non-overlapping ships should not have collision risk.");
    }

    @Test
    @DisplayName("Should not throw when printing ships list")
    void testPrintShips() {
        List<IShip> ships = List.of(new Barge(Compass.NORTH, new Position(1, 1)));
        assertDoesNotThrow(() -> fleet.printShips(ships));
    }

    @Test
    @DisplayName("Should not throw when printing fleet status")
    void testPrintStatus() {
        IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
        fleet.addShip(ship);

        assertDoesNotThrow(fleet::printStatus);
    }

    @Test
    @DisplayName("Should not throw when printing ships by category")
    void testPrintShipsByCategory() {
        fleet.addShip(new Barge(Compass.NORTH, new Position(1, 1)));
        assertDoesNotThrow(() -> fleet.printShipsByCategory("Barca"));
    }

    @Test
    @DisplayName("Should not throw when printing floating ships")
    void testPrintFloatingShips() {
        fleet.addShip(new Barge(Compass.NORTH, new Position(1, 1)));
        assertDoesNotThrow(() -> fleet.printFloatingShips());
    }

    @Test
    @DisplayName("Should not throw when printing all ships")
    void testPrintAllShips() {
        fleet.addShip(new Barge(Compass.NORTH, new Position(1, 1)));
        assertDoesNotThrow(() -> fleet.printAllShips());
    }

    @Test
    @DisplayName("Should create a random fleet with the correct number of ships")
    void testCreateRandom() {
        IFleet randomFleet = Fleet.createRandom();

        assertNotNull(randomFleet);
        assertEquals(Fleet.FLEET_SIZE, randomFleet.getShips().size());
    }
}