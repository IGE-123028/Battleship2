package battleship.ui;

import battleship.BoardColor;

public class ConsoleBoardRenderer {

	private ConsoleBoardRenderer() {
		// Utility class
	}

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
