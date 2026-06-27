package main.java.core;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class CalendarServer {
    public HttpResponse<String> fetchData(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public List<FeastDay> fetchFeastDays(String url) throws Exception {
        List<FeastDay> feastDays = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        HttpResponse<String> response = fetchData(url);
        JsonNode root = mapper.readTree(response.body());

        LocalDate date;
        String season;
        int seasonWeek;
        String weekday;

        for (JsonNode day : root) {
            date = LocalDate.parse(day.get("date").asText());
            season = day.get("season").asText();
            seasonWeek = day.get("season_week").asInt();

            List<Celebration> celebrations = new ArrayList<>();

            JsonNode celebrationsList = day.get("celebrations");
            for (JsonNode celebration : celebrationsList) {
                String title = celebration.get("title").asText();
                String color = celebration.get("colour").asText();
                String rank = celebration.get("rank").asText();
                double rankNum = celebration.get("rank_num").asDouble();

                celebrations.add(new Celebration(title, color, rank, rankNum));
            }
            weekday = day.get("weekday").asText();

            feastDays.add(new FeastDay(date, season, seasonWeek, celebrations, weekday));
        }

        return feastDays;
    }
}
