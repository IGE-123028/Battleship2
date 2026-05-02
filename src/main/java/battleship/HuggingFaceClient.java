package battleship;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

/**
 * Client for interacting with Hugging Face Inference API via Router.
 */
public class HuggingFaceClient {
    private static final String DEFAULT_MODEL = "Qwen/Qwen2.5-72B-Instruct";
    private final String apiKey;
    private final String model;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public HuggingFaceClient(String apiKey) {
        this(apiKey, DEFAULT_MODEL);
    }

    public HuggingFaceClient(String apiKey, String model) {
        this.apiKey = apiKey;
        this.model = model;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String chat(String prompt) throws Exception {
        String apiUrl = getApiUrl();

        String jsonBody = createRequestBody(prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("HTTP error " + response.statusCode() + ": " + response.body());
        }

        JsonNode rootNode = objectMapper.readTree(response.body());
        JsonNode choices = rootNode.path("choices");

        if (choices.isArray() && !choices.isEmpty()) {
            return choices.get(0).path("message").path("content").asText();
        }

        return "";
    }

    private String createRequestBody(String prompt) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        body.put("model", this.model);

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);

        body.put("messages", messages);
        body.put("max_tokens", 500);
        body.put("temperature", 0.1);

        String jsonBody = objectMapper.writeValueAsString(body);
        return jsonBody;
    }

    private static @NotNull String getApiUrl() {
        String apiUrl = "https://router.huggingface.co/v1/chat/completions";
        return apiUrl;
    }
}