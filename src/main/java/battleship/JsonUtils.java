package battleship;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;

/**
 * Small JSON utility used to extract and normalize the "rajada" array
 * from an LLM response. It is conservative and falls back to the
 * previous string-based extraction when parsing fails.
 */
public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() { }

    public static String extractRajadaArray(String response) throws Exception {
        if (response == null) throw new IllegalArgumentException("response is null");

        // First attempt: parse JSON robustly with Jackson
        try {
            JsonNode root = MAPPER.readTree(response);

            // If the whole response is already an array, return it
            if (root.isArray()) {
                return MAPPER.writeValueAsString(root);
            }

            // Look for a field named 'rajada' anywhere in the tree
            JsonNode rajada = root.findValue("rajada");
            if (rajada != null) {
                if (rajada.isArray()) return MAPPER.writeValueAsString(rajada);
                if (rajada.isTextual()) {
                    // in case the model returned the array as a string
                    String text = rajada.asText();
                    JsonNode parsed = tryParse(text);
                    if (parsed != null && parsed.isArray()) return MAPPER.writeValueAsString(parsed);
                }
            }

            // If not found, search for the first array node anywhere in the tree
            JsonNode firstArray = findFirstArrayNode(root);
            if (firstArray != null) return MAPPER.writeValueAsString(firstArray);

        } catch (JsonProcessingException e) {
            // fall through to fallback
        }

        // Fallback: best-effort substring extraction (keeps previous behaviour)
        int arrayStartMarker = response.indexOf("\"rajada\"");
        int start = -1;

        if (arrayStartMarker != -1) {
            start = response.indexOf("[", arrayStartMarker);
        } else {
            start = response.indexOf("[");
        }

        int end = response.lastIndexOf("]");

        if (start != -1 && end != -1 && start < end) {
            return response.substring(start, end + 1).trim();
        }

        throw new IllegalArgumentException("Unable to extract rajada array from LLM response");
    }

    private static JsonNode tryParse(String text) {
        try {
            return MAPPER.readTree(text);
        } catch (Exception e) {
            return null;
        }
    }

    private static JsonNode findFirstArrayNode(JsonNode node) {
        if (node == null) return null;
        if (node.isArray()) return node;
        if (node.isObject()) {
            Iterator<JsonNode> it = node.elements();
            while (it.hasNext()) {
                JsonNode child = it.next();
                JsonNode found = findFirstArrayNode(child);
                if (found != null) return found;
            }
        }
        return null;
    }
}
