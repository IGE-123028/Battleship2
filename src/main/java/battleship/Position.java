package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a position on the game board.
 * A position is defined by its row and column coordinates,
 * and it can be occupied or hit during the game.
 */
public class Position implements IPosition {

	/**
	 * The row coordinate of the position.
	 */
	private final int row;

	/**
	 * The column coordinate of the position.
	 */
	private final int column;

	/**
	 * Indicates whether the position is occupied by a ship.
	 */
	private boolean isOccupied;

	/**
	 * Indicates whether the position has been hit by an attack.
	 */
	private boolean isHit;

	//------------------------------------------------------------------
	public static Position randomPosition() {
		// Generate random position on the board
		int row = (int) (Math.random() * Game.BOARD_SIZE);
		int col = (int) (Math.random() * Game.BOARD_SIZE);
		return new Position(row, col);
	}
	/**
	 * Constructs a new Position with the specified row and column.
	 * By default, the position is not occupied and not hit.
	 *
	 * @param classicRow    the row coordinate of the position
	 * @param classicColumn the column coordinate of the position
	 */
	public Position(char classicRow, int classicColumn) {
		this.row = Character.toUpperCase(classicRow) - 'A';
		this.column = classicColumn-1;
		this.isOccupied = false;
		this.isHit = false;
	}

	/**
	 * Constructs a new Position with the specified row and column.
	 * By default, the position is not occupied and not hit.
	 *
	 * @param row    the row coordinate of the position
	 * @param column the column coordinate of the position
	 */
	public Position(int row, int column) {
		this.row = row;
		this.column = column;
		this.isOccupied = false;
		this.isHit = false;
	}

	/**
	 * Returns the row coordinate of the position.
	 *
	 * @return the row coordinate
	 */
	@Override
	public int getRow() {
		return row;
	}

	/**
	 * Returns the column coordinate of the position.
	 *
	 * @return the column coordinate
	 */
	@Override
	public int getColumn() {
		return column;
	}

	/**
	 * Gets traditional row.
	 *
	 * @return the traditional row within [A-J]
	 */
	public char getClassicRow() {
		return (char) ('A' + row);
	}

	/**
	 * Gets traditional column.
	 *
	 * @return the traditional column within [1-10]
	 */
	public int getClassicColumn() {
		return column + 1;
	}
	/**
	 * Checks if this position is valid on the game board.
	 * A position is "inside" if its row and column are within the board's boundaries.
	 *
	 * @return true if the position is within the board, false otherwise
	 */
	@Override
	public boolean isInside() {
		return row >= 0 && column >= 0 && row < Game.BOARD_SIZE && column < Game.BOARD_SIZE;
	}

	/**
	 * Checks if this position is adjacent to another position.
	 * Two positions are adjacent if they are next to each other horizontally, vertically, or diagonally.
	 *
	 * @param other the other position to compare
	 * @return true if the positions are adjacent, false otherwise
	 */
	@Override
	public boolean isAdjacentTo(IPosition other) {
		return Math.abs(this.row - other.getRow()) <= 1 && Math.abs(this.column - other.getColumn()) <= 1;
	}

	/**
	 * Returns all valid adjacent positions (up, right, down, left) for this position.
	 * A valid position is one that exists within the board boundaries.
	 * @return List of valid adjacent positions
	 */
	@Override
	public List<IPosition> adjacentPositions() {

		List<IPosition> adjacents = new ArrayList<IPosition>();

		int row = this.getRow();
		int col = this.getColumn();

		// Define possible directions (up, right, down, left)
		int[][] directions = {
				{-1, 0},  // north
				{0, 1},   // east
				{1, 0},   // south
				{0, -1},   // west
				{1, 1},   // northeast
				{1, -1},  // northwest
				{-1, 1},  // southeast
				{-1, -1} // southwest
		};

		// Check each possible direction
		for (int[] dir : directions) {
			Position newPosition = new Position(row + dir[0], col + dir[1]);
			// Only add the position if it's inside the board boundaries
			if (newPosition.isInside()) {
				adjacents.add(newPosition);
			}
		}

		return adjacents;
	}

	/**
	 * Checks if this position is occupied by a ship.
	 *
	 * @return true if the position is occupied, false otherwise
	 */
	@Override
	public boolean isOccupied() {
		return isOccupied;
	}

	/**
	 * Checks if this position has been hit by an attack.
	 *
	 * @return true if the position is hit, false otherwise
	 */
	@Override
	public boolean isHit() {
		return isHit;
	}

	/**
	 * Marks this position as occupied by a ship.
	 */
	@Override
	public void occupy() {
		isOccupied = true;
	}

	/**
	 * Marks this position as hit by an attack.
	 */
	@Override
	public void shoot() {
		isHit = true;
	}

	/**
	 * Compares this position to another object for equality.
	 * Two positions are equal if their row and column coordinates are the same.
	 *
	 * @param otherPosition the object to compare
	 * @return true if the positions are equal, false otherwise
	 */
	@Override
	public boolean equals(Object otherPosition) {
		if (this == otherPosition) {
			return true;
		}
		if (otherPosition instanceof IPosition) {
			IPosition other = (IPosition) otherPosition;
			return this.row == other.getRow() && this.column == other.getColumn();
		}
		return false;
	}

	/**
	 * Returns a hash code for this position based on its row and column.
	 *
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		return Objects.hash(row, column, isOccupied, isHit);
	}

	/**
	 * Returns a string representation of this position.
	 * The string includes the row and column coordinates.
	 *
	 * @return the string representation of the position
	 */
	@Override
	public String toString() {
		return (char) ('A' + row) + "" + (column + 1);
//		return "Row = " + (char) ('A' + row) + ", Column = " + (column + 1);
	}
}