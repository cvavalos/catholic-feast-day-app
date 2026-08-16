package ui;
import core.CalendarServer;
import core.DatabaseManager;
import core.FeastDay;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javafx.geometry.Pos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarController {
    public BorderPane getView() {
        try {
            controlDatabase();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        DatabaseManager databaseManager = new DatabaseManager();

        FeastDay todaysDay = databaseManager.selectFeastDay(LocalDate.now().toString());

        BorderPane root = new BorderPane();
        HBox searchBar = new HBox(10);
        VBox feastDay = new VBox(10);
        GridPane grid = new GridPane();

        TextField text = new TextField();
        Button searchButton = new Button("Search");
        searchBar.getChildren().add(text);
        searchBar.getChildren().add(searchButton);
        searchBar.setAlignment(Pos.CENTER);

        Label feast = new Label("Today's Feast");
        Label date = new Label(todaysDay.getDate().toString());
        feastDay.getChildren().add(feast);
        feastDay.getChildren().add(date);

        root.setTop(searchBar);
        root.setLeft(grid);
        root.setCenter(feastDay);

        return root;
    }

    public List<FeastDay> fetchDataFromAPI() throws Exception {
        CalendarServer calendarServer = new CalendarServer();
        List<FeastDay> allFeastDays = new ArrayList<FeastDay>();

        List<FeastDay> januaryFeastDays = calendarServer.fetchFeastDays("http://calapi.inadiutorium.cz/api/v0/en/calendars/default/2026/1");
        List<FeastDay> februaryFeastDays = calendarServer.fetchFeastDays("http://calapi.inadiutorium.cz/api/v0/en/calendars/default/2026/2");
        List<FeastDay> marchFeastDays = calendarServer.fetchFeastDays("http://calapi.inadiutorium.cz/api/v0/en/calendars/default/2026/3");
        List<FeastDay> aprilFeastDays = calendarServer.fetchFeastDays("http://calapi.inadiutorium.cz/api/v0/en/calendars/default/2026/4");
        List<FeastDay> mayFeastDays = calendarServer.fetchFeastDays("http://calapi.inadiutorium.cz/api/v0/en/calendars/default/2026/5");
        List<FeastDay> juneFeastDays = calendarServer.fetchFeastDays("http://calapi.inadiutorium.cz/api/v0/en/calendars/default/2026/6");
        List<FeastDay> julyFeastDays = calendarServer.fetchFeastDays("http://calapi.inadiutorium.cz/api/v0/en/calendars/default/2026/7");
        List<FeastDay> augustFeastDays = calendarServer.fetchFeastDays("http://calapi.inadiutorium.cz/api/v0/en/calendars/default/2026/8");
        List<FeastDay> septemberFeastDays = calendarServer.fetchFeastDays("http://calapi.inadiutorium.cz/api/v0/en/calendars/default/2026/9");
        List<FeastDay> octoberFeastDays = calendarServer.fetchFeastDays("http://calapi.inadiutorium.cz/api/v0/en/calendars/default/2026/10");
        List<FeastDay> novemberFeastDays = calendarServer.fetchFeastDays("http://calapi.inadiutorium.cz/api/v0/en/calendars/default/2026/11");
        List<FeastDay> decemberFeastDays = calendarServer.fetchFeastDays("http://calapi.inadiutorium.cz/api/v0/en/calendars/default/2026/12");

        allFeastDays.addAll(januaryFeastDays);
        allFeastDays.addAll(februaryFeastDays);
        allFeastDays.addAll(marchFeastDays);
        allFeastDays.addAll(aprilFeastDays);
        allFeastDays.addAll(mayFeastDays);
        allFeastDays.addAll(juneFeastDays);
        allFeastDays.addAll(julyFeastDays);
        allFeastDays.addAll(augustFeastDays);
        allFeastDays.addAll(septemberFeastDays);
        allFeastDays.addAll(octoberFeastDays);
        allFeastDays.addAll(novemberFeastDays);
        allFeastDays.addAll(decemberFeastDays);

        return allFeastDays;
    }

    public void controlDatabase() throws Exception {
        DatabaseManager databaseManager = new DatabaseManager();

        databaseManager.runQuery("CREATE TABLE IF NOT EXISTS feast_days (id INTEGER PRIMARY KEY, date TEXT, season TEXT, season_week INTEGER, weekday TEXT)");
        databaseManager.runQuery("CREATE TABLE IF NOT EXISTS celebrations (title TEXT, color TEXT, rank TEXT, rank_num REAL, feast_day_id INTEGER, FOREIGN KEY (feast_day_id) REFERENCES feast_days(id))");

        if (!databaseManager.hasFeastDays()) {
            List<FeastDay> allFeastDays = fetchDataFromAPI();
            for (FeastDay feastDay : allFeastDays) {
                databaseManager.insertFeastDays(feastDay);
            }
        }
    }
}
