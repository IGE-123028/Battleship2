package battleship;

/**
 * The type Caravel represents a ship with a size of 2 units.
 * It is positioned on the game board based on its bearing and initial position.
 *
 * Author: britoeabreu
 * Date: 2023-10-10
 * Time: 15:30
 */
public class Caravel extends Ship {

	/**
	 * Instantiates a new Caravel.
	 *
	 * @param bearing The bearing of the ship (NORTH, SOUTH, EAST, or WEST).
	 * @param pos     The initial position of the ship on the game board.
	 */
	public Caravel(Compass bearing, IPosition pos) {
		super("Caravela", bearing, pos, 2);

		switch (bearing) {
			case NORTH:
				addVerticalPositions(pos);
				break;
			case SOUTH:
				addVerticalPositions(pos);
				break;
			case EAST:
				addHorizontalPositions(pos);
				break;
			case WEST:
				addHorizontalPositions(pos);
				break;
		}
	}

	private void addHorizontalPositions(IPosition pos) {
		for (int c = 0; c < this.getSize(); c++)
			getPositions().add(new Position(pos.getRow(), pos.getColumn() + c));
	}

	private void addVerticalPositions(IPosition pos) {
		for (int r = 0; r < this.getSize(); r++)
			getPositions().add(new Position(pos.getRow() + r, pos.getColumn()));
	}
}