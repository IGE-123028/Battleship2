package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Test class for the Galleon class.
 * Author: ${user.name}
 * Date: ${current_date}
 * Time: ${current_time}
 * Cyclomatic Complexity for each method:
 * - Constructor: 5
 * - stillFloating: 2
 * - getPositions: 2
 * - getTopMostPos: 2
 * - getBottomMostPos: 2
 * - getLeftMostPos: 2
 * - getRightMostPos: 2
 */
public class GalleonTest {

	private Galleon galleon;

	@BeforeEach
	void setUp() {
		galleon = new Galleon(Compass.NORTH, new Position(5, 5));
	}

	@AfterEach
	void tearDown() {
		galleon = null;
	}

	/**
	 * Test for the constructor with NORTH direction.
	 * Cyclomatic Complexity: 5
	 */
	@Test
	void testConstructorNorth() {
		List<IPosition> positions = galleon.getPositions();
		assertNotNull(galleon, "Error: Galleon instance should not be null.");
		assertEquals(5, positions.size(), "Error: Galleon should have exactly 5 positions.");
		assertEquals(new Position(5, 5), positions.get(0), "Error: The first position is incorrect for NORTH.");
		assertEquals(new Position(5, 6), positions.get(1), "Error: The second position is incorrect for NORTH.");
		assertEquals(new Position(5, 7), positions.get(2), "Error: The third position is incorrect for NORTH.");
		assertEquals(new Position(6, 6), positions.get(3), "Error: The fourth position is incorrect for NORTH.");
		assertEquals(new Position(7, 6), positions.get(4), "Error: The fifth position is incorrect for NORTH.");
	}

	/**
	 * Test for the constructor with SOUTH direction.
	 */
	@Test
	void testConstructorSouth() {
		Galleon southGalleon = new Galleon(Compass.SOUTH, new Position(5, 5));
		List<IPosition> positions = southGalleon.getPositions();
		assertNotNull(southGalleon, "Error: Galleon instance should not be null.");
		assertEquals(5, positions.size(), "Error: Galleon should have exactly 5 positions.");
		assertEquals(new Position(5, 5), positions.get(0), "Error: The first position is incorrect for SOUTH.");
		assertEquals(new Position(6, 5), positions.get(1), "Error: The second position is incorrect for SOUTH.");
		assertEquals(new Position(7, 4), positions.get(2), "Error: The third position is incorrect for SOUTH.");
		assertEquals(new Position(7, 5), positions.get(3), "Error: The fourth position is incorrect for SOUTH.");
		assertEquals(new Position(7, 6), positions.get(4), "Error: The fifth position is incorrect for SOUTH.");
	}

	/**
	 * Test for the stillFloating method (all positions intact).
	 * Cyclomatic Complexity: 2
	 */
	@Test
	void testStillFloating1() {
		assertTrue(galleon.stillFloating(), "Error: Galleon should initially be floating.");
	}

	/**
	 * Test for the stillFloating method (all positions hit).
	 */
	@Test
	void testStillFloating2() {
		galleon.getPositions().forEach(pos -> pos.shoot());
		assertFalse(galleon.stillFloating(), "Error: Galleon should not float when all positions are hit.");
	}

	/**
	 * Test for the getTopMostPos method.
	 * Cyclomatic Complexity: 2
	 */
	@Test
	void testGetTopMostPos() {
		assertEquals(5, galleon.getTopMostPos(), "Error: The topmost position should be 5.");
	}

	/**
	 * Test for the getBottomMostPos method.
	 * Cyclomatic Complexity: 2
	 */
	@Test
	void testGetBottomMostPos() {
		assertEquals(7, galleon.getBottomMostPos(), "Error: The bottommost position should be 7.");
	}

	/**
	 * Test for the getLeftMostPos method.
	 * Cyclomatic Complexity: 2
	 */
	@Test
	void testGetLeftMostPos() {
		assertEquals(5, galleon.getLeftMostPos(), "Error: The leftmost position should be 5.");
	}

	/**
	 * Test for the getRightMostPos method.
	 * Cyclomatic Complexity: 2
	 */
	@Test
	void testGetRightMostPos() {
		assertEquals(7, galleon.getRightMostPos(), "Error: The rightmost position should be 7.");
	}

	/**
	 * Test for the constructor with invalid input.
	 */
	@Test
	void testConstructorWithInvalidInput() {
		assertThrows(NullPointerException.class, () -> new Galleon(null, null),
				"Error: NullPointerException should have been thrown for null input.");
		assertThrows(NullPointerException.class, () -> new Galleon(Compass.NORTH, null),
				"Error: NullPointerException should have been thrown when position is null.");
	}

	/**
	 * Test for the constructor with EAST direction.
	 */
	@Test
	void testConstructorEast() {
		Galleon eastGalleon = new Galleon(Compass.EAST, new Position(5, 5));
		List<IPosition> positions = eastGalleon.getPositions();
		assertNotNull(eastGalleon, "Error: Galleon instance should not be null.");
		assertEquals(5, positions.size(), "Error: Galleon should have exactly 5 positions.");
		assertEquals(new Position(5, 5), positions.get(0), "Error: The first position is incorrect for EAST.");
		assertEquals(new Position(6, 3), positions.get(1), "Error: The second position is incorrect for EAST.");
		assertEquals(new Position(6, 4), positions.get(2), "Error: The third position is incorrect for EAST.");
		assertEquals(new Position(6, 5), positions.get(3), "Error: The fourth position is incorrect for EAST.");
		assertEquals(new Position(7, 5), positions.get(4), "Error: The fifth position is incorrect for EAST.");
	}

	/**
	 * Test for the constructor with WEST direction.
	 */
	@Test
	void testConstructorWest() {
		Galleon westGalleon = new Galleon(Compass.WEST, new Position(5, 5));
		List<IPosition> positions = westGalleon.getPositions();
		assertNotNull(westGalleon, "Error: Galleon instance should not be null.");
		assertEquals(5, positions.size(), "Error: Galleon should have exactly 5 positions.");
		assertEquals(new Position(5, 5), positions.get(0), "Error: The first position is incorrect for WEST.");
		assertEquals(new Position(6, 5), positions.get(1), "Error: The second position is incorrect for WEST.");
		assertEquals(new Position(6, 6), positions.get(2), "Error: The third position is incorrect for WEST.");
		assertEquals(new Position(6, 7), positions.get(3), "Error: The fourth position is incorrect for WEST.");
		assertEquals(new Position(7, 5), positions.get(4), "Error: The fifth position is incorrect for WEST.");
	}
}