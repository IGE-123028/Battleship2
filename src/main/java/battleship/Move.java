package battleship;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.*;

public class Move implements IMove {

    private final int number;
    private final List<IPosition> shots;
    private final List<IGame.ShotResult> shotResults;

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

        Map<String, Object> data = calculateResults();

        if (verbose) {
            System.out.println("Jogada nº" + this.number);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar o JSON dos resultados da jogada", e);
        }
    }

    private Map<String, Object> calculateResults() {

        int validShots = 0;
        int repeatedShots = 0;
        int missedShots = 0;

        Map<String, Integer> sunkBoatsCount = new HashMap<>();
        Map<String, Integer> hitsPerBoat = new HashMap<>();

        for (IGame.ShotResult result : this.shotResults) {
            if (!result.valid()) {
                continue;
            }

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

        Map<String, Object> response = new HashMap<>();
        response.put("validShots", validShots);
        response.put("outsideShots", outsideShots);
        response.put("repeatedShots", repeatedShots);
        response.put("missedShots", missedShots);

        response.put("sunkBoatsCount", sunkBoatsCount);
        response.put("hitsPerBoat", hitsPerBoat);

        return response;
    }
}
