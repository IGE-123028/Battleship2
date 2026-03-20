package battleship;

import java.util.List;

public class LLMService {
    private final HuggingFaceClient client;
    private final String initialPrompt;

    public LLMService(String apiKey) {
        this.client = new HuggingFaceClient(apiKey);
        this.initialPrompt = """
                CONTEXT AND ROLE

                    You are an expert strategist in the game Battleship (Portuguese Discoveries version).
                    Your goal is to sink the entire enemy fleet using the fewest possible shots.

                CORE OBJECTIVE

                    Maximize efficiency. Victory is not about speed, but about minimizing the total number of shots.

                CRITICAL RULES (MANDATORY)

                    Your response MUST be ONLY a valid JSON object
                    DO NOT include any text outside the JSON
                    DO NOT use markdown (```json)
                    Each response MUST contain exactly 3 shots

                    Coordinates MUST be valid:
                    Rows: A–J
                    Columns: 1–10

                    If ANY rule is violated, the response is invalid.

                    HARD CONSTRAINTS (ENFORCED)

                    NEVER repeat a coordinate that has already been used

                    NEVER shoot outside the board

                    ONLY repeat shots if the game is already finished (to complete the 3 required shots)

                    ALWAYS ensure coordinates are unique within the same response

                    INVALID or duplicate coordinates are strictly forbidden

                    INTERNAL MEMORY (LOGBOOK – DO NOT OUTPUT)

                    You must maintain an internal Logbook tracking:
                    Volley number (Volley 1, 2, 3...)
                    Coordinates fired
                    Result of each shot (Miss, Hit, Sunk, Ship type)
                    Use this memory to:
                    Avoid duplicate shots
                    Infer ship positions
                    Mark blocked zones (halo around sunk ships)
                    DO NOT include this Logbook in your response.

                TACTICAL RULES

                    If a shot hits a ship → prioritize adjacent positions (North, South, East, West) in the next move
                    If a ship is confirmed sunk → DO NOT target adjacent cells (ships never touch)
                    Ships (Caravel, Nau, Frigate) are straight lines (horizontal or vertical)
                    Therefore, diagonals from a hit are almost always water
                    ONLY consider diagonals in the case of a Galleon (T-shaped ship)
                    Once a ship is sunk:
                        Identify all its positions using your Logbook
                        Mark all surrounding cells (halo) as invalid targets

                SEARCH STRATEGY (HYBRID)

                    Use a non-predictable, adaptive approach:
                    Divide the board into 4 quadrants
                    Start near the center of a quadrant
                    Use a parity system (cells classified as 0 and 1)
                    Select one parity and stick with it initially

                Pattern movement:

                    Jump 3 cells in one direction
                    Then shift 1 cell orthogonally
                    Continue in a spiral-like pattern (example: D3 → G4 → F7 → C6)
                    This creates a loose rectangular sweep
                    Then shift the pattern diagonally and repeat (example: F1 → I2 → H5 → E4)
                    Adjust the pattern dynamically. Do NOT follow rigid or predictable sequences.

                EFFICIENCY PRINCIPLES

                    Do NOT blindly chase every hit immediately
                    Continue exploration until at least ~3 ships are located
                    Avoid linear or checkerboard-only strategies
                    Avoid predictable patterns that opponents can exploit

                REASONING FIELD

                    The "raciocinio" field MUST contain:
                    A short explanation (1–2 sentences)
                    The strategy used (e.g., exploration, pattern, continuation)
                    DO NOT include detailed chain-of-thought

                INPUT

                    You will receive a History (external Logbook) containing the results of the previous volley.

                OUTPUT FORMAT (STRICT)

                    {
                    "raciocinio": "Short explanation of the tactical decision.",
                    "rajada": [
                    {"row": "A", "column": 1},
                    {"row": "B", "column": 2},
                    {"row": "C", "column": 3}
                    ]
                    }

                ENDGAME RULE

                If the enemy fleet is fully sunk:
                    Continue returning exactly 3 shots
                    Repeated coordinates are allowed ONLY in this situation
                    Indicate this clearly in the reasoning
                    FINAL VALIDATION (MANDATORY BEFORE RESPONDING)
                    All 3 coordinates are unique
                    No coordinate has been used before
                    All coordinates are within A–J and 1–10
                    JSON is valid and properly formatted

                If ANY condition fails → correct it before responding.
                """;
    }

    public String getNextMove(Game game) throws Exception {
        StringBuilder history = new StringBuilder();
        history.append("Histórico de tiros (Diário de Bordo):\n");

        List<IMove> alienMoves = game.getAlienMoves();
        if (alienMoves.isEmpty()) {
            history.append("Nenhuma rajada efetuada ainda. Todos os espaços de A1 a J10 podem ser atirados.\n");
        } else {
            for (IMove move : alienMoves) {
                history.append("Rajada ").append(move.getNumber()).append(": ").append(Game.jsonShots(move.getShots()))
                        .append("\n");
                history.append("Relatório: ").append(move.processEnemyFire(false)).append("\n\n");
            }
        }

        String fullPrompt = initialPrompt + "\n" + history.toString()
                + "\nO seu output estritamente em JSON (com 'raciocinio' e 'rajada'):";

        String response = client.chat(fullPrompt);
        return cleanJsonResponse(response);
    }

    private String cleanJsonResponse(String response) {
        int arrayStartMarker = response.indexOf("\"rajada\"");
        int start = -1;

        if (arrayStartMarker != -1) {
            start = response.indexOf("[", arrayStartMarker);
        } else {
            start = response.indexOf("[");
        }

        int end = response.lastIndexOf("]");

        if (start != -1 && end != -1 && start < end) {
            return response.substring(start, end + 1);
        }
        return response.trim();
    }
}
