package battleship;

/**
 * The type Galleon represents a ship with a size of 5 units.
 * It is positioned on the game board based on its bearing and initial position.
 * The Galleon has a unique shape depending on its orientation.
 *
 * Author: britoeabreu
 * Date: 2023-10-10
 * Time: 15:30
 */
public class Galleon extends Ship {

	/**
	 * Instantiates a new Galleon.
	 *
	 * @param bearing The bearing of the ship (NORTH, SOUTH, EAST, or WEST).
	 * @param pos     The initial position of the ship on the game board.
	 */
	public Galleon(Compass bearing, IPosition pos) {
		super("Galeao", bearing, pos, 5);

		switch (bearing) {
			case NORTH:
				fillNorth(pos);
				break;
			case EAST:
				fillEast(pos);
				break;
			case SOUTH:
				fillSouth(pos);
				break;
			case WEST:
				fillWest(pos);
				break;
		}
	}

	/**
	 * Fills the positions of the Galleon when oriented to the NORTH.
	 *
	 * @param pos The initial position of the ship.
	 */
	private void fillNorth(IPosition pos) {
		for (int i = 0; i < 3; i++) {
			getPositions().add(new Position(pos.getRow(), pos.getColumn() + i));
		}
		getPositions().add(new Position(pos.getRow() + 1, pos.getColumn() + 1));
		getPositions().add(new Position(pos.getRow() + 2, pos.getColumn() + 1));
	}

	/**
	 * Fills the positions of the Galleon when oriented to the SOUTH.
	 *
	 * @param pos The initial position of the ship.
	 */
	private void fillSouth(IPosition pos) {
		for (int i = 0; i < 2; i++) {
			getPositions().add(new Position(pos.getRow() + i, pos.getColumn()));
		}
		for (int j = 2; j < 5; j++) {
			getPositions().add(new Position(pos.getRow() + 2, pos.getColumn() + j - 3));
		}
	}

	/**
	 * Fills the positions of the Galleon when oriented to the EAST.
	 *
	 * @param pos The initial position of the ship.
	 */
	private void fillEast(IPosition pos) {
		getPositions().add(new Position(pos.getRow(), pos.getColumn()));
		for (int i = 1; i < 4; i++) {
			getPositions().add(new Position(pos.getRow() + 1, pos.getColumn() + i - 3));
		}
		getPositions().add(new Position(pos.getRow() + 2, pos.getColumn()));
	}

	/**
	 * Fills the positions of the Galleon when oriented to the WEST.
	 *
	 * @param pos The initial position of the ship.
	 */
	private void fillWest(IPosition pos) {
		getPositions().add(new Position(pos.getRow(), pos.getColumn()));
		for (int i = 1; i < 4; i++) {
			getPositions().add(new Position(pos.getRow() + 1, pos.getColumn() + i - 1));
		}
		getPositions().add(new Position(pos.getRow() + 2, pos.getColumn()));
	}
}