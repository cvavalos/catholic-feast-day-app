package ui;
import core.CalendarServer;
import core.DatabaseManager;
import core.FeastDay;
import core.Celebration;

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
import java.time.Month;
import java.time.YearMonth;
import java.time.DayOfWeek;

import java.util.ArrayList;
import java.util.List;

public class CalendarController {
    int displayedYear = LocalDate.now().getYear();
    Month displayedMonth = LocalDate.now().getMonth();
    DayOfWeek displayedDay = LocalDate.now().getDayOfWeek();

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

        HBox searchBar = manageSearchBar();
        VBox feastDay = manageFeastDayBox(todaysDay);
        GridPane grid = manageCalendar();

        VBox calendar = new VBox();
        HBox navigationBar = manageNavigationBar();
        VBox gridHolder = new VBox();

        calendar.getChildren().add(navigationBar);
        calendar.getChildren().add(gridHolder);

        gridHolder.getChildren().add(grid);

        root.setTop(searchBar);
        root.setLeft(calendar);
        root.setCenter(feastDay);

        return root;
    }

    public HBox manageSearchBar() {
        HBox searchBar = new HBox(10);

        TextField text = new TextField();
        Button searchButton = new Button("Search");
        searchBar.getChildren().add(text);
        searchBar.getChildren().add(searchButton);
        searchBar.setAlignment(Pos.CENTER);

        return searchBar;
    }

    public VBox manageFeastDayBox(FeastDay todaysDay) {
        VBox feastDay = new VBox(10);

        Label feast = new Label("Today's Feast");
        Label date = new Label(todaysDay.getDate().toString());
        Label season = new Label(todaysDay.getSeason());
        Label season_week = new Label(Integer.toString(todaysDay.getSeasonWeek()));
        Label celebrationIntro = new Label("Celebrations");
        Label weekday = new Label(todaysDay.getWeekday());

        feastDay.getChildren().add(feast);
        feastDay.getChildren().add(date);
        feastDay.getChildren().add(season);
        feastDay.getChildren().add(season_week);
        feastDay.getChildren().add(celebrationIntro);

        for (Celebration celebration : todaysDay.getCelebrations()) {
            List<Label> celebrations = new ArrayList<Label>();
            celebrations.add(new Label(celebration.getTitle()));
            celebrations.add(new Label(celebration.getColor()));
            celebrations.add(new Label(celebration.getRank()));
            celebrations.add(new Label(String.valueOf(celebration.getRankNum())));
            celebrations.add(new Label(Integer.toString(celebration.getFeast_day_id())));

            for (Label label : celebrations) {
                feastDay.getChildren().add(label);
            }
        }
        feastDay.getChildren().add(weekday);

        return feastDay;
    }

    public GridPane manageCalendar() {
        GridPane grid = new GridPane();

        displayedYear = LocalDate.now().getYear();
        displayedMonth = LocalDate.now().getMonth();
        int day = 1;

        YearMonth month = YearMonth.of(displayedYear, displayedMonth);
        int daysInMonth = month.lengthOfMonth();
        int row = 0;
        int rowTracker = getDayOfWeekNum(LocalDate.of(displayedYear, displayedMonth, day).getDayOfWeek());

        for (int i = 0; i < daysInMonth; i++) {
            LocalDate date = LocalDate.of(displayedYear, displayedMonth, day);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            int dayOfWeekNum = getDayOfWeekNum(dayOfWeek);

            if (rowTracker > 6) {
                row++;
                rowTracker = 0;
            }

            Label label = new Label(String.valueOf(day));
            grid.add(label, dayOfWeekNum, row);
           rowTracker++;
           day++;
        }

        return grid;
    }

    public HBox manageNavigationBar() {
        HBox navigationBar = new HBox();
        Button leftMonthNavigator;
        Button rightMonthNavigator;
        Button leftDayNavigator;
        Button rightDayNavigator;

        String monthToDisplay = getMonth(displayedMonth);

        Label month = new Label(monthToDisplay + " " + Integer.toString(displayedYear));

        navigationBar.getChildren().add(month);

        return navigationBar;
    }

    public String getMonth(Month month) {
        switch (month) {
            case JANUARY:
                return "January";
            case FEBRUARY:
                return "February";
            case MARCH:
                return "March";
            case APRIL:
                return "April";
            case MAY:
                return "May";
            case JUNE:
                return "June";
            case JULY:
                return "July";
            case AUGUST:
                return "August";
            case SEPTEMBER:
                return "September";
            case OCTOBER:
                return "October";
            case NOVEMBER:
                return "November";
            case DECEMBER:
                return "December";
            default:
                return "null";
        }
    }

    public int getDayOfWeekNum(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case SUNDAY:
                return 0;
            case MONDAY:
                return 1;
            case TUESDAY:
                return 2;
            case WEDNESDAY:
                return 3;
            case THURSDAY:
                return 4;
            case FRIDAY:
                return 5;
            case SATURDAY:
                return 6;
            default:
                return 7;
        }
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
