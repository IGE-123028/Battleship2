package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * IMove
 *
 * @author Your Name
 * Date: 26/02/2026
 * Time: 18:32
 */
public interface IMove {
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

	@Override
	String toString();

	int getNumber();

	List<IPosition> getShots();

	boolean hasShot(IPosition pos);

	List<IGame.ShotResult> getShotResults();

	String processEnemyFire(boolean verbose);
}
