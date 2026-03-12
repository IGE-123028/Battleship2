package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Barge class.
 * Author: britoeabreu
 * Date: 2025-11-11 16:40
 * Cyclomatic Complexity:
 * - Constructor: 1
 * - getSize: 1
 * - stillFloating: 2
 * - getPositions: 1
 * - shoot: 2
 * - occupies: 2
 * - tooCloseTo (IShip): 2
 * - tooCloseTo (IPosition): 2
 * - getTopMostPos: 1
 * - getBottomMostPos: 1
 * - getLeftMostPos: 1
 * - getRightMostPos: 1
 */
public class BargeTest {

	private Barge barge;

	@BeforeEach
	void setUp() {
		barge = new Barge(Compass.NORTH, new Position(5, 5));
	}

	@AfterEach
	void tearDown() {
		barge = null;
	}

	@Test
	void constructor() {
		assertNotNull(barge, "Error: Barge instance should not be null.");
		assertEquals("Barca", barge.getCategory(), "Error: Barge category should be 'Barca'.");
		assertEquals(Compass.NORTH, barge.getBearing(), "Error: Barge bearing is incorrect.");
		assertEquals(1, barge.getSize(), "Error: Barge size should be 1.");
		assertEquals(1, barge.getPositions().size(), "Error: Barge should have exactly one position.");
		assertEquals(new Position(5, 5), barge.getPositions().get(0), "Error: Barge position is incorrect.");
	}

	@Test
	void getSize() {
		assertEquals(1, barge.getSize(), "Error: Barge size should be 1.");
	}

	@Test
	void stillFloating1() {
		assertTrue(barge.stillFloating(), "Error: Barge should still be floating.");
	}

	@Test
	void stillFloating2() {
		barge.getPositions().get(0).shoot();
		assertFalse(barge.stillFloating(), "Error: Barge should not float after being hit.");
	}

	@Test
	void getPositions() {
		assertEquals(1, barge.getPositions().size(), "Error: Barge should have exactly one position.");
		assertEquals(new Position(5, 5), barge.getPositions().get(0), "Error: Barge position is incorrect.");
	}

	@Test
	void shoot1() {
		barge.shoot(new Position(5, 5));
		assertTrue(barge.getPositions().get(0).isHit(), "Error: The position should be hit.");
	}

	@Test
	void shoot2() {
		barge.shoot(new Position(1, 1));
		assertFalse(barge.getPositions().get(0).isHit(), "Error: Shot should not affect the barge for invalid positions.");
	}

	@Test
	void occupies1() {
		assertTrue(barge.occupies(new Position(5, 5)), "Error: Barge should occupy position (5, 5).");
	}

	@Test
	void occupies2() {
		assertFalse(barge.occupies(new Position(1, 1)), "Error: Barge should not occupy position (1, 1).");
	}

	@Test
	void tooCloseToShip1() {
		Barge nearbyBarge = new Barge(Compass.NORTH, new Position(5, 6));
		assertTrue(barge.tooCloseTo(nearbyBarge), "Error: Barges should be too close.");
	}

	@Test
	void tooCloseToShip2() {
		Barge distantBarge = new Barge(Compass.NORTH, new Position(10, 10));
		assertFalse(barge.tooCloseTo(distantBarge), "Error: Barges should not be too close.");
	}

	@Test
	void tooCloseToPosition1() {
		assertTrue(barge.tooCloseTo(new Position(5, 6)), "Error: Barge should be too close to adjacent position (5, 6).");
	}

	@Test
	void tooCloseToPosition2() {
		assertFalse(barge.tooCloseTo(new Position(7, 7)), "Error: Barge should not be too close to non-adjacent position (7, 7).");
	}

	@Test
	void getTopMostPos() {
		assertEquals(5, barge.getTopMostPos(), "Error: The topmost position should be 5.");
	}

	@Test
	void getBottomMostPos() {
		assertEquals(5, barge.getBottomMostPos(), "Error: The bottommost position should be 5.");
	}

	@Test
	void getLeftMostPos() {
		assertEquals(5, barge.getLeftMostPos(), "Error: The leftmost position should be 5.");
	}

	@Test
	void getRightMostPos() {
		assertEquals(5, barge.getRightMostPos(), "Error: The rightmost position should be 5.");
	}

	@Test
	void constructorNullInput() {
		assertThrows(NullPointerException.class, () -> new Barge(null, null),
				"Error: NullPointerException should be thrown for null input.");
		assertThrows(NullPointerException.class, () -> new Barge(Compass.NORTH, null),
				"Error: NullPointerException should be thrown for null position.");
	}
}