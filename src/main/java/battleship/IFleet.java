/**
 * 
 */
package battleship;

import java.util.List;

/**
 * The interface Fleet.
 */
public interface IFleet
{
	/**
	 * The constant FLEET_SIZE.
	 */
	Integer FLEET_SIZE = 11;

	/**
	 * Gets ships.
	 *
	 * @return the ships
	 */
	List<IShip> getShips();

	/**
	 * Add ship boolean.
	 *
	 * @param s the s
	 * @return the boolean
	 */
	boolean addShip(IShip s);

	/**
	 * Gets ships like.
	 *
	 * @param category the category
	 * @return the ships like
	 */
	List<IShip> getShipsLike(String category);

	/**
	 * Gets floating ships.
	 *
	 * @return the floating ships
	 */
	List<IShip> getFloatingShips();

	/**
	 * Gets sunk ships.
	 *
	 * @return the sunk ships
	 */
	List<IShip> getSunkShips();

	/**
	 * Ship at ship.
	 *
	 * @param pos the pos
	 * @return the ship
	 */
	IShip shipAt(IPosition pos);

	/**
	 * Print status.
	 */
	void printStatus();
}
