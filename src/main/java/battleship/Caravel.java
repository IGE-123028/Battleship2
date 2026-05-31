package battleship;

/**
 * The type Caravel represents a ship with a size of 2 units.
 * It is positioned on the game board based on its bearing and initial position.
 *
 * Author: britoeabreu
 * Date: 2023-10-10
 * Time: 15:30
 */
public class Caravel extends LinearShip {

	/**
	 * Instantiates a new Caravel.
	 *
	 * @param bearing The bearing of the ship (NORTH, SOUTH, EAST, or WEST).
	 * @param pos     The initial position of the ship on the game board.
	 */
	public Caravel(Compass bearing, IPosition pos) {
		super("Caravela", bearing, pos, 2);
	}
}