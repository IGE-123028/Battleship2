package battleship.ui;

import battleship.BoardColor;

/**
 * Utility class for rendering board markers in the console with appropriate colors.
 */
public class ConsoleBoardRenderer {

	private ConsoleBoardRenderer() {
		// Utility class
	}

	/**
	 * Returns a colored string representation of the given board marker.
	 *
	 * @param marker the board marker to color (e.g., '#', '*', 'o', '-', '.')
	 * @return a colored string for console display
	 */
	public static String colored(char marker) {
		return switch (marker) {
			case '#' -> BoardColor.ship();
			case '*' -> BoardColor.hit();
			case 'o' -> BoardColor.miss();
			case '-' -> BoardColor.adjacent();
			case '.' -> BoardColor.water();
			default -> String.valueOf(marker);
		};
	}
}
