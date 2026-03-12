/**
 * 
 */
package battleship;

import java.util.List;

/**
 * The interface Ship.
 */
public interface IShip
{
	/**
	 * Gets category.
	 *
	 * @return the category
	 */
	String getCategory();

	/**
	 * Gets size.
	 *
	 * @return the size
	 */
	Integer getSize();

	/**
	 * Gets positions.
	 *
	 * @return the positions
	 */
	List<IPosition> getPositions();

	/**
	 * Gets positions.
	 *
	 * @return the adjacent positions
	 */
	List<IPosition> getAdjacentPositions();

	/**
	 * Gets position.
	 *
	 * @return the position
	 */
	IPosition getPosition();

	/**
	 * Gets bearing.
	 *
	 * @return the bearing
	 */
	Compass getBearing();

	/**
	 * Still floating boolean.
	 *
	 * @return the boolean
	 */
	boolean stillFloating();

	/**
	 * Gets top most pos.
	 *
	 * @return the top most pos
	 */
	int getTopMostPos();

	/**
	 * Gets bottom most pos.
	 *
	 * @return the bottom most pos
	 */
	int getBottomMostPos();

	/**
	 * Gets left most pos.
	 *
	 * @return the left most pos
	 */
	int getLeftMostPos();

	/**
	 * Gets right most pos.
	 *
	 * @return the right most pos
	 */
	int getRightMostPos();

	/**
	 * Occupies boolean.
	 *
	 * @param pos the pos
	 * @return the boolean
	 */
	boolean occupies(IPosition pos);

	/**
	 * Too close to boolean.
	 *
	 * @param other the other
	 * @return the boolean
	 */
	boolean tooCloseTo(IShip other);

	/**
	 * Too close to boolean.
	 *
	 * @param pos the pos
	 * @return the boolean
	 */
	boolean tooCloseTo(IPosition pos);

	/**
	 * Shoot.
	 *
	 * @param pos the pos
	 */
	void shoot(IPosition pos);

	/**
	 * Sink.
	 */
	void sink();
}
