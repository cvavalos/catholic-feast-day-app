package main.java.core;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import main.java.core.FeastDay;
import java.util.Date;

public class CalendarServer {
    public HttpResponse<String> fetchData(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public FeastDay sendFeastDay(String url) throws Exception {
        FeastDay day = new FeastDay();
        ObjectMapper mapper = new ObjectMapper();
        HttpResponse<String> response = fetchData(url);
        JsonNode root = mapper.readTree(json);



        return day;
    }
}
