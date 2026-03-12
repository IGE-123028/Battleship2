/**
 * Author: britoeabreu
 * Date: 2023-10-10
 * Time: 15:30
 *
 * Cyclomatic Complexity:
 * - Constructor: 5
 * - getSize: 1
 */
package battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for the Caravel class.
 */
@DisplayName("Tests for the Caravel class")
class CaravelTest {

	static Caravel cN, cS, cE, cW;

	@BeforeAll
	static void setUpBeforeClass() {
		cN = new Caravel(Compass.NORTH, new Position(5, 5));
		cS = new Caravel(Compass.SOUTH, new Position(5, 5));
		cE = new Caravel(Compass.EAST, new Position(5, 5));
		cW = new Caravel(Compass.WEST, new Position(5, 5));
	}

	@AfterAll
	static void tearDownAfterClass() {
		cN = null;
		cS = null;
		cE = null;
		cW = null;
	}

	/**
	 * Test for the getSize method.
	 * Cyclomatic Complexity: 1
	 */
	@Test
	@DisplayName("Test for the getSize method")
	void getSize() {
		assertEquals(2, cN.getSize(), "Error: The size of the Caravel should be 2.");
	}

	/**
	 * Test for the constructor with NORTH direction.
	 * Cyclomatic Complexity: 5
	 */
	@Test
	@DisplayName("Test for the constructor with NORTH direction")
	void constructor1() {
		assertNotNull(cN, "Error: The Caravel should not be null.");
		assertEquals(Compass.NORTH, cN.getBearing(), "Error: The Caravel's direction should be NORTH.");
		assertEquals(5, cN.getTopMostPos(), "Error: The topmost position should be 5.");
	}

	/**
	 * Test for the constructor with SOUTH direction.
	 */
	@Test
	@DisplayName("Test for the constructor with SOUTH direction")
	void constructor2() {
		assertNotNull(cS, "Error: The Caravel should not be null.");
		assertEquals(Compass.SOUTH, cS.getBearing(), "Error: The Caravel's direction should be SOUTH.");
		assertEquals(6, cS.getBottomMostPos(), "Error: The bottommost position should be 6.");
	}

	/**
	 * Test for the constructor with EAST direction.
	 */
	@Test
	@DisplayName("Test for the constructor with EAST direction")
	void constructor3() {
		assertNotNull(cE, "Error: The Caravel should not be null.");
		assertEquals(Compass.EAST, cE.getBearing(), "Error: The Caravel's direction should be EAST.");
		assertEquals(6, cE.getRightMostPos(), "Error: The rightmost position should be 6.");
	}

	/**
	 * Test for the constructor with WEST direction.
	 */
	@Test
	@DisplayName("Test for the constructor with WEST direction")
	void constructor4() {
		assertNotNull(cW, "Error: The Caravel should not be null.");
		assertEquals(Compass.WEST, cW.getBearing(), "Error: The Caravel's direction should be WEST.");
		assertEquals(5, cW.getLeftMostPos(), "Error: The leftmost position should be 5.");
	}

	/**
	 * Test for the constructor with an invalid direction.
	 */
	@Test
	@DisplayName("Test for the constructor with an invalid direction")
	void constructor5() {
		assertThrows(NullPointerException.class, () -> new Caravel(null, new Position(0, 0)),
				"Error: An IllegalArgumentException should have been thrown for an invalid direction.");
	}

	/**
	 * Test for the constructor with null values.
	 */
	@Test
	@DisplayName("Test for the constructor with null values")
	void constructorNullPointerException() {
		Exception exception = assertThrows(NullPointerException.class, () ->
				new Caravel(null, new Position(0, 0)), "Error: A NullPointerException should have been thrown for a null direction.");
		assertEquals("Ship's bearing must not be null", exception.getMessage(),
				"Error: The exception message does not match the expected value.");

		Exception exception2 = assertThrows(NullPointerException.class, () ->
				new Caravel(null, null), "Error: A NullPointerException should have been thrown for null direction and position.");
		assertEquals("Ship's bearing must not be null", exception2.getMessage(),
				"Error: The exception message does not match the expected value.");
	}
}