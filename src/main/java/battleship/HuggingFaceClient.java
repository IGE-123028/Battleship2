package battleship;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Client for interacting with Hugging Face Inference API.
 */
public class HuggingFaceClient {
    private static final String DEFAULT_MODEL = "mistralai/Mistral-7B-Instruct-v0.3";
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
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public String chat(String prompt) throws Exception {
        String apiUrl = "https://api-inference.huggingface.co/models/" + model;
        
        Map<String, Object> body = new HashMap<>();
        body.put("inputs", prompt);
        
        // Some models prefer "parameters"
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("max_new_tokens", 500);
        parameters.put("return_full_text", false);
        body.put("parameters", parameters);

        String jsonBody = objectMapper.writeValueAsString(body);

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

        // HF Inference API returns a list of objects like [{"generated_text": "..."}]
        List<Map<String, Object>> result = objectMapper.readValue(response.body(), List.class);
        if (result.isEmpty()) {
            return "";
        }
        return (String) result.get(0).get("generated_text");
    }
}
