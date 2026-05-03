package evaluator.network;

import com.google.gson.Gson;
import evaluator.model.AnalysisResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8000";
    private static final int    TIMEOUT  = 120;

    private final HttpClient httpClient;
    private final Gson gson;

    public ApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT))
                .build();
        this.gson = new Gson();
    }

    public AnalysisResponse analyze(String code, String arrayInput,
                                     String sizeInput, String mode) throws Exception {

        // Build request body
        AnalysisRequest requestBody = new AnalysisRequest(code, arrayInput, sizeInput, mode);
        String jsonBody = gson.toJson(requestBody);

        // Build HTTP POST request 
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/analyze"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(TIMEOUT))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Send request and get response 
        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Server error: " + response.statusCode()
                    + "\n" + response.body());
        }

        // Convert JSON response → AnalysisResponse object
        return gson.fromJson(response.body(), AnalysisResponse.class);
    }

    private static class AnalysisRequest {
        private final String code;
        private final String array;
        private final String size;
        private final String mode;

        public AnalysisRequest(String code, String array, String size, String mode) {
            this.code  = code;
            this.array = array;
            this.size  = size;
            this.mode  = mode;
        }
    }
}