package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interface representing a move in the Battleship game.
 * A move consists of multiple shots fired in a single volley.
 */
public interface IMove {
	/**
	 * Reads a move from the provided scanner.
	 *
	 * @param moveNumber the sequence number of the move
	 * @param sc         the scanner to read from
	 * @return a new Move instance
	 */
	static Move readMove(int moveNumber, Scanner sc) {
		int numShots = sc.nextInt();
		List<IPosition> moveShots = new ArrayList<>();
		for (int i = 0; i < numShots; i++) {
			int row = sc.nextInt();
			int col = sc.nextInt();
			moveShots.add(new Position(row, col));
		}
		return new Move(moveNumber, moveShots, new ArrayList<>());
	}

	/**
	 * Returns a string representation of the move.
	 *
	 * @return string representation
	 */
	@Override
	String toString();

	/**
	 * Gets the move number.
	 *
	 * @return the sequence number of this move
	 */
	int getNumber();

	/**
	 * Gets the list of shots fired in this move.
	 *
	 * @return list of shot positions
	 */
	List<IPosition> getShots();

	/**
	 * Checks if this move includes a shot at the specified position.
	 *
	 * @param pos the position to check
	 * @return true if a shot was fired at pos in this move
	 */
	boolean hasShot(IPosition pos);

	/**
	 * Gets the results of each shot in this move.
	 *
	 * @return list of shot results
	 */
	List<IGame.ShotResult> getShotResults();

	/**
	 * Processes the results of the shots and generates a descriptive string.
	 *
	 * @param verbose if true, prints a detailed message to the console
	 * @return a JSON string with the summarized results
	 */
	String processEnemyFire(boolean verbose);
}
