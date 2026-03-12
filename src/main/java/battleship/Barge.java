package battleship;

/**
 * The type Barge represents a ship with a size of 1 unit.
 * It is positioned on the game board based on its bearing and initial position.
 *
 * Author: britoeabreu
 * Date: 2023-10-10
 * Time: 15:30
 */
public class Barge extends Ship {

	/**
	 * Instantiates a new Barge.
	 *
	 * @param bearing The bearing of the barge (NORTH, SOUTH, EAST, or WEST).
	 * @param pos     The upper-left position of the barge on the game board.
	 */
	public Barge(Compass bearing, IPosition pos) {
		super("Barca", bearing, pos, 1);

		// Add the single position of the barge to the list of positions
		getPositions().add(new Position(pos.getRow(), pos.getColumn()));
	}
}