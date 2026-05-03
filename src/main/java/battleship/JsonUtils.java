package battleship;

/**
 * Utility class for JSON-related operations.
 */
public class JsonUtils {

    /**
     * Cleans a JSON response string by extracting the array part.
     *
     * @param response the raw response string
     * @return the cleaned JSON string
     */
    public static String cleanJsonResponse(String response) {
        if (response == null) {
            return null;
        }

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
