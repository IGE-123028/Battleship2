package battleship;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Position.
 * Author: britoeabreu
 * Date: 2024-03-19 15:30
 * Cyclomatic Complexity for each method:
 * - Constructor: 1
 * - getRow: 1
 * - getColumn: 1
 * - isValid: 4
 * - isAdjacentTo: 4
 * - isOccupied: 1
 * - isHit: 1
 * - occupy: 1
 * - shoot: 1
 * - equals: 3
 * - hashCode: 1
 * - toString: 1
 */

@DisplayName("Tests for Position class")
public class PositionTest {
	private Position position;

	@BeforeEach
	void setUp() {
		position = new Position(2, 3);
	}

	@AfterEach
	void tearDown() {
		position = null;
	}

	@Test
	@DisplayName("Constructor should initialize position with numeric coordinates")
	void constructor1() {
		Position pos = new Position(1, 1);
		assertNotNull(pos);
		assertEquals(1, pos.getRow());
		assertEquals(1, pos.getColumn());
		assertFalse(pos.isOccupied());
		assertFalse(pos.isHit());
	}

	@Test
	@DisplayName("Constructor should initialize position with classic coordinates")
	void constructor2() {
		Position pos = new Position('C', 1);
		assertNotNull(pos);
		assertEquals('C', pos.getClassicRow());
		assertEquals(1, pos.getClassicColumn());
		assertFalse(pos.isOccupied());
		assertFalse(pos.isHit());
	}

	@Test
	@DisplayName("getRow should return correct row value")
	void getRow() {
		assertEquals(2, position.getRow());
	}

	@Test
	@DisplayName("getColumn should return correct column value")
	void getColumn() {
		assertEquals(3, position.getColumn());
	}

	@Test
	@DisplayName("getClassicRow should return correct classic row")
	void getClassicRow() {
		assertEquals('C', position.getClassicRow());
	}

	@Test
	@DisplayName("getClassicColumn should return correct classic column")
	void getClassicColumn() {
		assertEquals(4, position.getClassicColumn());
	}

	@Test
	@DisplayName("Position (0,0) should be inside the board")
	void isValid1() {
		position = new Position(0, 0);
		assertTrue(position.isInside());
	}

	@Test
	@DisplayName("Negative row should be outside the board")
	void isValid2() {
		position = new Position(-1, 5);
		assertFalse(position.isInside());
	}

	@Test
	@DisplayName("Negative column should be outside the board")
	void isValid3() {
		position = new Position(5, -1);
		assertFalse(position.isInside());
	}

	@Test
	@DisplayName("Row >= BOARD_SIZE should be outside the board")
	void isValid4() {
		position = new Position(Game.BOARD_SIZE, 5);
		assertFalse(position.isInside());
	}

	@Test
	@DisplayName("Column >= BOARD_SIZE should be outside the board")
	void isValid5() {
		position = new Position(5, Game.BOARD_SIZE);
		assertFalse(position.isInside());
	}

	@Test
	@DisplayName("Should detect horizontally adjacent position")
	void isAdjacentTo1() {
		Position other = new Position(2, 4);
		assertTrue(position.isAdjacentTo(other));
	}

	@Test
	@DisplayName("Should detect vertically adjacent position")
	void isAdjacentTo2() {
		Position other = new Position(3, 3);
		assertTrue(position.isAdjacentTo(other));
	}

	@Test
	@DisplayName("Should detect diagonally adjacent position")
	void isAdjacentTo3() {
		Position other = new Position(3, 4);
		assertTrue(position.isAdjacentTo(other));
	}

	@Test
	@DisplayName("Should return false for non-adjacent positions")
	void isAdjacentTo4() {
		Position other = new Position(4, 5);
		assertFalse(position.isAdjacentTo(other));
	}

	@Test
	@DisplayName("isAdjacentTo should throw NullPointerException when null is passed")
	void isAdjacentToWithNull() {
		assertThrows(NullPointerException.class, () -> position.isAdjacentTo(null));
	}

	@Test
	@DisplayName("Position should become occupied after calling occupy()")
	void isOccupied() {
		assertFalse(position.isOccupied());
		position.occupy();
		assertTrue(position.isOccupied());
	}

	@Test
	@DisplayName("Position should be marked as hit after calling shoot()")
	void isHit() {
		assertFalse(position.isHit());
		position.shoot();
		assertTrue(position.isHit());
	}

	@Test
	@DisplayName("Equals should return true for identical positions")
	void equals1() {
		Position same = new Position(2, 3);
		assertTrue(position.equals(same));
	}

	@Test
	@DisplayName("Equals should return false when compared with null")
	void equals2() {
		assertFalse(position.equals(null));
	}

	@Test
	@DisplayName("Equals should return false when compared with different type")
	void equals3() {
		assertFalse(position.equals(new Object()));
	}

	@Test
	@DisplayName("Equals should return false for different positions")
	void equals4() {
		Position other = new Position(2, 4);
		assertFalse(position.equals(other));
	}

	@Test
	@DisplayName("Equals should return true when comparing same instance")
	void equals5() {
		assertTrue(position.equals(position));
	}

	@Test
	@DisplayName("HashCode should be consistent for equal objects")
	void hashCodeConsistency() {
		Position same = new Position(2, 3);
		assertEquals(position.hashCode(), same.hashCode());
	}

	@Test
	@DisplayName("toString should return correct format (e.g., C4)")
	void toStringFormat() {
		String expected = "C4";
		assertEquals(expected, position.toString());
	}

	@Test
	@DisplayName("Should return all 8 adjacent positions for a middle cell")
	void adjacentPositions() {
		List<IPosition> adjacents = position.adjacentPositions();
		assertEquals(8, adjacents.size());
	}

	@Test
	@DisplayName("Should return only valid adjacent positions on board edge")
	public void testAdjacentPositionsEdge() {
		Position position = new Position(0, 5);
		List<IPosition> adjacents = position.adjacentPositions();
		assertEquals(5, adjacents.size());
	}
}