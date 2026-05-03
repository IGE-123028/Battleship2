package battleship;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.*;

/**
 * Shot
 *
 * @author Your Name
 *         Date: 20/02/2026
 *         Time: 19:39
 */
public class Move implements IMove {

	// -------------------------------------------------------------------
	private final int number;
	private final List<IPosition> shots;
	private final List<IGame.ShotResult> shotResults;

	// -------------------------------------------------------------------
	public Move(int moveNumber, List<IPosition> moveShots, List<IGame.ShotResult> moveResults) {
		this.number = moveNumber;
		this.shots = moveShots;
		this.shotResults = moveResults;
	}

	@Override
	public String toString() {
		return "Move{" +
				"number=" + number +
				", shots=" + shots.size() +
				", results=" + shotResults.size() +
				'}';
	}

	@Override
	public int getNumber() {
		return this.number;
	}

	@Override
	public List<IPosition> getShots() {
		return this.shots;
	}

	@Override
	public boolean hasShot(IPosition pos) {
		return this.shots.contains(pos);
	}

	@Override
	public List<IGame.ShotResult> getShotResults() {
		return this.shotResults;
	}

	/**
	 * Processes the results of enemy fire on the game board, analyzing the outcomes
	 * of shots,
	 * such as valid shots, repeated shots, missed shots, hits on ships, and sunk
	 * ships. It can
	 * also display a detailed summary of the shot results if verbose mode is
	 * activated.
	 *
	 * @param verbose a boolean indicating whether a detailed summary should be
	 *                printed to the console
	 *                for the processed enemy fire data.
	 * @return a JSON-formatted string that encapsulates the results, including
	 *         counts of valid shots,
	 *         repeated shots, missed shots, shots outside the game board, and
	 *         details of hits and
	 *         sunk ships.
	 */
	@Override
	public String processEnemyFire(boolean verbose) {

		int validShots = 0;
		int repeatedShots = 0;
		int missedShots = 0;

		Map<String, Integer> sunkBoatsCount = new HashMap<>();
		Map<String, Integer> hitsPerBoat = new HashMap<>();

		// Processar cada resultado de tiro
		for (IGame.ShotResult result : this.shotResults) {
			if (!result.valid())
				continue;

			if (result.repeated()) {
				repeatedShots++;
			} else {
				validShots++;
				if (result.ship() == null) {
					missedShots++;
				} else {
					String boatName = result.ship().getCategory();
					hitsPerBoat.put(boatName, hitsPerBoat.getOrDefault(boatName, 0) + 1);
					if (result.sunk()) {
						sunkBoatsCount.put(boatName, sunkBoatsCount.getOrDefault(boatName, 0) + 1);
					}
				}
			}
		}

		int outsideShots = Game.NUMBER_SHOTS - validShots - repeatedShots;

		if (verbose) {
			buildVerboseMessage(validShots, repeatedShots, missedShots, outsideShots, sunkBoatsCount, hitsPerBoat);
		}

		return generateJsonResponse(validShots, outsideShots, repeatedShots, missedShots, sunkBoatsCount, hitsPerBoat);
	}

	private void buildVerboseMessage(int validShots, int repeatedShots, int missedShots, int outsideShots,
			Map<String, Integer> sunkBoatsCount, Map<String, Integer> hitsPerBoat) {
		StringBuilder output = new StringBuilder();

		if (validShots == 0 && repeatedShots > 0) {
			output.append(repeatedShots).append(" tiro").append(repeatedShots > 1 ? "s" : "").append(" repetido")
					.append(repeatedShots > 1 ? "s" : "");
		} else {
			if (validShots > 0) {
				output.append(validShots).append(" tiro").append(validShots > 1 ? "s" : "").append(" válido")
						.append(validShots > 1 ? "s" : "").append(": ");
			}

			appendSunkBoats(output, sunkBoatsCount);
			appendBoatHits(output, sunkBoatsCount, hitsPerBoat);

			if (missedShots > 0) {
				output.append(missedShots).append(" tiro").append(missedShots > 1 ? "s" : "").append(" na água");
			} else if (!sunkBoatsCount.isEmpty() || !hitsPerBoat.isEmpty()) {
				output.setLength(output.length() - 2); // Remove trailing " + "
			}

			appendRepeatedAndOutsideShots(output, validShots, repeatedShots, outsideShots);
		}

		System.out.println("Jogada nº" + this.number + " -> " + output);
	}

	private void appendSunkBoats(StringBuilder output, Map<String, Integer> sunkBoatsCount) {
		for (Map.Entry<String, Integer> entry : sunkBoatsCount.entrySet()) {
			String boatName = entry.getKey();
			int count = entry.getValue();
			output.append(count).append(" ").append(boatName).append(count > 1 ? "s" : "").append(" ao fundo")
					.append(" + ");
		}
	}

	private void appendBoatHits(StringBuilder output, Map<String, Integer> sunkBoatsCount,
			Map<String, Integer> hitsPerBoat) {
		for (Map.Entry<String, Integer> entry : hitsPerBoat.entrySet()) {
			String boatName = entry.getKey();
			int hits = entry.getValue();
			if (!sunkBoatsCount.containsKey(boatName)) {
				output.append(hits).append(" tiro").append(hits > 1 ? "s" : "").append(" num(a) ").append(boatName)
						.append(" + ");
			}
		}
	}

	private void appendRepeatedAndOutsideShots(StringBuilder output, int validShots, int repeatedShots,
			int outsideShots) {
		if (repeatedShots > 0) {
			if (validShots > 0)
				output.append(", ");
			output.append(repeatedShots).append(" tiro").append(repeatedShots > 1 ? "s" : "").append(" repetido")
					.append(repeatedShots > 1 ? "s" : "");
		}

		if (outsideShots > 0) {
			if (!output.isEmpty())
				output.append(", ");
			output.append(outsideShots).append(" tiro").append(outsideShots > 1 ? "s" : "").append(" exterior")
					.append(outsideShots > 1 ? "es" : "");
		}
	}

	private String generateJsonResponse(int validShots, int outsideShots, int repeatedShots, int missedShots,
			Map<String, Integer> sunkBoatsCount, Map<String, Integer> hitsPerBoat) {
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("validShots", validShots);
		responseMap.put("outsideShots", outsideShots);
		responseMap.put("repeatedShots", repeatedShots);
		responseMap.put("missedShots", missedShots);

		responseMap.put("sunkBoats", getSunkBoatsList(sunkBoatsCount));
		responseMap.put("hitsOnBoats", getBoatHitsList(sunkBoatsCount, hitsPerBoat));

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		try {
			return objectMapper.writeValueAsString(responseMap);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Erro ao serializar o JSON dos resultados da jogada", e);
		}
	}

	private List<Map<String, Object>> getSunkBoatsList(Map<String, Integer> sunkBoatsCount) {
		List<Map<String, Object>> sunkBoats = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : sunkBoatsCount.entrySet()) {
			Map<String, Object> boat = new HashMap<>();
			boat.put("type", entry.getKey());
			boat.put("count", entry.getValue());
			sunkBoats.add(boat);
		}
		return sunkBoats;
	}

	private List<Map<String, Object>> getBoatHitsList(Map<String, Integer> sunkBoatsCount,
			Map<String, Integer> hitsPerBoat) {
		List<Map<String, Object>> boatHits = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : hitsPerBoat.entrySet()) {
			if (!sunkBoatsCount.containsKey(entry.getKey())) {
				Map<String, Object> boat = new HashMap<>();
				boat.put("type", entry.getKey());
				boat.put("hits", entry.getValue());
				boatHits.add(boat);
			}
		}
		return boatHits;
	}

}
