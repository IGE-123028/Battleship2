package battleship;

import java.util.List;

/**
 * The type Carrack represents a ship with a size of 3 units.
 * It is positioned on the game board based on its bearing and initial position.
 *
 * Author: britoeabreu
 * Date: 2023-10-10
 * Time: 15:30
 */
public class Carrack extends LinearShip {

	/**
	 * Instantiates a new Carrack.
	 *
	 * @param bearing The bearing of the ship (NORTH, SOUTH, EAST, or WEST).
	 * @param pos     The initial position of the ship on the game board.
	 */
	public Carrack(Compass bearing, IPosition pos) {
		super("Nau", bearing, pos, 3);
	}
}