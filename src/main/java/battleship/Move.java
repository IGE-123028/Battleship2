package battleship;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.*;

/**
 * Class representing a move in the game, including the volley of shots and their results.
 */
public class Move implements IMove {

    private static final String TIRO_LITERAL = " tiro";

    // -------------------------------------------------------------------
    private final int number;
    private final List<IPosition> shots;
    private final List<IGame.ShotResult> shotResults;

    // -------------------------------------------------------------------
    /**
     * Constructs a new Move with the specified move number, shots, and results.
     *
     * @param moveNumber  the sequence number of the move
     * @param moveShots   the list of positions targeted
     * @param moveResults the results of the shots
     */
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
            output.append(getTiro(repeatedShots)).append(repeatedShots > 1 ? " repetidos" : " repetido");
        } else {
            appendMainShots(output, validShots, missedShots, sunkBoatsCount, hitsPerBoat);
            appendRepeatedAndOutsideShots(output, validShots, repeatedShots, outsideShots);
        }

        System.out.println("Jogada nº" + this.number + " -> " + output);
    }

    private void appendMainShots(StringBuilder output, int validShots, int missedShots,
                                 Map<String, Integer> sunkBoatsCount, Map<String, Integer> hitsPerBoat) {
        if (validShots > 0) {
            output.append(getTiro(validShots)).append(validShots > 1 ? " válidos: " : " válido: ");
        }

        appendSunkBoats(output, sunkBoatsCount);
        appendBoatHits(output, sunkBoatsCount, hitsPerBoat);

        if (missedShots > 0) {
            output.append(getTiro(missedShots)).append(" na água");
        } else if (!sunkBoatsCount.isEmpty() || !hitsPerBoat.isEmpty()) {
            output.setLength(output.length() - 2); // Remove trailing " + "
        }
    }

    private String getTiro(int count) {
        return count + TIRO_LITERAL + (count > 1 ? "s" : "");
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
                output.append(getTiro(hits)).append(" num(a) ").append(boatName)
                        .append(" + ");
            }
        }
    }

    private void appendRepeatedAndOutsideShots(StringBuilder output, int validShots, int repeatedShots,
                                               int outsideShots) {
        if (repeatedShots > 0) {
            if (validShots > 0)
                output.append(", ");
            output.append(getTiro(repeatedShots)).append(repeatedShots > 1 ? " repetidos" : " repetido");
        }

        if (outsideShots > 0) {
            if (!output.isEmpty())
                output.append(", ");
            output.append(getTiro(outsideShots)).append(outsideShots > 1 ? " exteriores" : " exterior");
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
