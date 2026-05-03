package battleship;

/**
 * The enum Compass.
 *
 * @author fba
 */
public enum Compass
{
	/**
	 * North compass.
	 */
	NORTH('n'),
	/**
	 * South compass.
	 */
	SOUTH('s'),
	/**
	 * East compass.
	 */
	EAST('e'),
	/**
	 * West compass.
	 */
	WEST('o');

	/**
	 * Generate a random compass direction (bearing).
	 *
	 * @return A random compass direction.
	 */
	public static Compass randomBearing() {
		Compass[] values = Compass.values();  // Get all enum values
		int randomIndex = (int) (Math.random() * values.length);  // Pick a random index
		return values[randomIndex];  // Return the random compass direction
	}


	/**
	 * The C.
	 */
	private final char c;

	/**
	 * Instantiates a new Compass.
	 *
	 * @param c the c
	 */
	Compass(char c)
    {
	this.c = c;
    }

	/**
	 * Gets direction.
	 *
	 * @return the direction
	 */
	public char getDirection()
    {
	return c;
    }

	/**
	 * To string string.
	 *
	 * @return the string
	 */
	@Override
    public String toString()
    {
	return "" + c;
    }

	/**
	 * Char to compass compass.
	 *
	 * @param ch the ch
	 * @return the compass
	 */
	static Compass charToCompass(char ch)
    {
		for (Compass compass : values())
			if (compass.getDirection() == ch)
				return compass;
		return null;
    }
}
