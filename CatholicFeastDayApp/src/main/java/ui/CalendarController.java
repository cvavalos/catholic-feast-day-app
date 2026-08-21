package ui;
import core.CalendarServer;
import core.DatabaseManager;
import core.FeastDay;
import core.Celebration;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.time.LocalDate;
import java.time.Year;
import java.time.Month;
import java.time.YearMonth;
import java.time.DayOfWeek;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class CalendarController {
    Year displayedYear = Year.now();
    Month displayedMonth = LocalDate.now().getMonth();
    DayOfWeek displayedDayOfWeek = LocalDate.now().getDayOfWeek();
    LocalDate displayedDay = LocalDate.now();
    VBox gridHolder;
    VBox feastDayBox;
    HBox navigationBar;

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
        feastDayBox = manageFeastDayBox(todaysDay);
        GridPane grid = manageCalendar(Year.now(), LocalDate.now().getMonth());

        VBox calendar = new VBox();
        Border calendarBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                BorderWidths.DEFAULT));

        navigationBar = manageNavigationBar(Year.now().toString(), getMonth(LocalDate.now().getMonth()));
        gridHolder = new VBox();

        calendar.getChildren().add(navigationBar);
        calendar.getChildren().add(gridHolder);
        calendar.setBorder(calendarBorder);

        gridHolder.getChildren().add(grid);

        root.setTop(searchBar);
        root.setLeft(calendar);
        root.setCenter(feastDayBox);

        return root;
    }

    public HBox manageSearchBar() {
        HBox searchBar = new HBox(10);
        Border searchBarBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                BorderWidths.DEFAULT));

        Label searchLabel = new Label("Search by date (ex.: 2026-08-26)");
        TextField text = new TextField();

        Button searchButton = manageSearchButton(text);

        searchBar.getChildren().add(searchLabel);
        searchBar.getChildren().add(text);
        searchBar.getChildren().add(searchButton);
        searchBar.setAlignment(Pos.CENTER);
        searchBar.setBorder(searchBarBorder);

        return searchBar;
    }

    public VBox manageFeastDayBox(FeastDay todaysDay) {
        VBox feastDay = new VBox(10);
        Border feastDayBoxBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                BorderWidths.DEFAULT));

        Label feast = new Label("Today's Feast");
        feast.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, 30));
        feast.setMaxWidth(Double.MAX_VALUE);
        feast.setAlignment(Pos.TOP_CENTER);

        Label date = new Label("Date: " + todaysDay.getDate().toString());
        date.setFont(Font.font("Bookman Old Style", 15));

        Label season = new Label("Season: " + todaysDay.getSeason());
        season.setFont(Font.font("Bookman Old Style", 15));

        Label season_week = new Label("Season Week: " + Integer.toString(todaysDay.getSeasonWeek()));
        season_week.setFont(Font.font("Bookman Old Style", 15));

        Label celebrationIntro = new Label("Celebrations");
        celebrationIntro.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, 20));

        Label weekday = new Label("Day of the Week: " + todaysDay.getWeekday());
        weekday.setFont(Font.font("Bookman Old Style", 15));

        feastDay.getChildren().add(feast);
        feastDay.getChildren().add(date);
        feastDay.getChildren().add(season);
        feastDay.getChildren().add(season_week);
        feastDay.getChildren().add(celebrationIntro);

        for (Celebration celebration : todaysDay.getCelebrations()) {
            List<Label> celebrations = new ArrayList<Label>();
            Label nameLabel = new Label("  - Name: " + celebration.getTitle());
            nameLabel.setFont(Font.font("Bookman Old Style", 15));

            Label colorLabel = new Label("  - Color: " + celebration.getColor());
            colorLabel.setFont(Font.font("Bookman Old Style", 15));

            Label rankLabel = new Label("  - Rank: " + celebration.getRank());
            rankLabel.setFont(Font.font("Bookman Old Style", 15));

            Label rankNumLabel = new Label("  - Rank Number: " + String.valueOf(celebration.getRankNum()));
            rankNumLabel.setFont(Font.font("Bookman Old Style", 15));

            celebrations.addAll(Arrays.asList(nameLabel, colorLabel, rankLabel, rankNumLabel));

            for (Label label : celebrations) {
                feastDay.getChildren().add(label);
            }
        }
        feastDay.getChildren().add(weekday);
        feastDay.setSpacing(20);
        feastDay.setBorder(feastDayBoxBorder);

        return feastDay;
    }

    public GridPane manageCalendar(Year year, Month month) {
        GridPane grid = new GridPane();
        Border gridBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                BorderWidths.DEFAULT));

        displayedYear = year;
        int thisYear = year.getValue();
        displayedMonth = month;
        int day = 1;

        YearMonth thisMonth = YearMonth.of(thisYear, displayedMonth);
        int daysInMonth = thisMonth.lengthOfMonth();
        int row = 0;
        int rowTracker = getDayOfWeekNum(LocalDate.of(thisYear, displayedMonth, day).getDayOfWeek());

        for (int i = 0; i < daysInMonth; i++) {
            LocalDate date = LocalDate.of(thisYear, displayedMonth, day);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            int dayOfWeekNum = getDayOfWeekNum(dayOfWeek);

            if (rowTracker > 6) {
                row++;
                rowTracker = 0;
            }

            Button button = manageDayButton(String.valueOf(day), date.toString(), date);

            button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            button.setBorder(gridBorder);

            grid.setHgrow(button, Priority.ALWAYS);
            grid.setVgrow(button, Priority.ALWAYS);

            grid.add(button, dayOfWeekNum, row);
            rowTracker++;
            day++;
        }

        grid.setBorder(gridBorder);

        return grid;
    }

    public HBox manageNavigationBar(String year, String monthToDisplay) {
        HBox navigation = new HBox();
        Border navigationBarBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                BorderWidths.DEFAULT));

        Button leftMonthNavigator = manageLeftMonthNavigator();
        Button rightMonthNavigator = manageRightMonthNavigator();
        Button leftDayNavigator = manageLeftDayNavigator();
        Button rightDayNavigator = manageRightDayNavigator();

        Label month = new Label(monthToDisplay + " " + year);

        navigation.getChildren().add(leftMonthNavigator);
        navigation.getChildren().add(leftDayNavigator);
        navigation.getChildren().add(month);
        navigation.getChildren().add(rightDayNavigator);
        navigation.getChildren().add(rightMonthNavigator);
        navigation.setBorder(navigationBarBorder);

        return navigation;
    }

    public Button manageLeftMonthNavigator() {
        Button leftMonthNavigator = new Button("<");

        leftMonthNavigator.setOnAction(event -> {
            if (displayedMonth == Month.JANUARY) {
                displayedYear = displayedYear.minusYears(1);
            }
            displayedMonth = displayedMonth.minus(1);

            gridHolder.getChildren().clear();
            gridHolder.getChildren().add(manageCalendar(displayedYear, displayedMonth));
            navigationBar.getChildren().clear();
            navigationBar.getChildren().add(manageNavigationBar(displayedYear.toString(), getMonth(displayedMonth)));
        });

        return leftMonthNavigator;
    }

    public Button manageRightMonthNavigator() {
        Button rightMonthNavigator = new Button(">");

        rightMonthNavigator.setOnAction(event -> {
            if (displayedMonth == Month.DECEMBER) {
                displayedYear = displayedYear.plusYears(1);
            }
            displayedMonth = displayedMonth.plus(1);

            gridHolder.getChildren().clear();
            gridHolder.getChildren().add(manageCalendar(displayedYear, displayedMonth));
            navigationBar.getChildren().clear();
            navigationBar.getChildren().add(manageNavigationBar(displayedYear.toString(), getMonth(displayedMonth)));
        });

        return rightMonthNavigator;
    }

    public Button manageLeftDayNavigator() {
        Button leftDayNavigator = new Button("<<");

        leftDayNavigator.setOnAction(event -> {
            displayedDay = displayedDay.minusDays(1);

            displayedMonth = displayedDay.getMonth();
            displayedYear = Year.of(displayedDay.getYear());

            navigationBar.getChildren().clear();
            navigationBar.getChildren().add(manageNavigationBar(displayedYear.toString(), getMonth(displayedMonth)));
        });

        return leftDayNavigator;
    }

    public Button manageRightDayNavigator() {
        Button rightDayNavigator = new Button(">>");

        rightDayNavigator.setOnAction(event -> {
            displayedDay = displayedDay.plusDays(1);

            displayedMonth = displayedDay.getMonth();
            displayedYear = Year.of(displayedDay.getYear());

            navigationBar.getChildren().clear();
            navigationBar.getChildren().add(manageNavigationBar(displayedYear.toString(), getMonth(displayedMonth)));
        });

        return rightDayNavigator;
    }

    public Button manageDayButton(String day, String date, LocalDate dateSelected) {
        Button dayButton = new Button(day);
        DatabaseManager databaseManager = new DatabaseManager();

        dayButton.setOnAction(event -> {
            FeastDay selectedDay = databaseManager.selectFeastDay(date);
            displayedYear = Year.of(dateSelected.getYear());
            displayedMonth = dateSelected.getMonth();
            displayedDay = dateSelected;

            feastDayBox.getChildren().clear();
            feastDayBox.getChildren().add(manageFeastDayBox(selectedDay));
        });

        return dayButton;
    }

    public Button manageSearchButton(TextField text) {
        Button searchButton = new Button("Search");
        DatabaseManager databaseManager = new DatabaseManager();

        searchButton.setOnAction(event -> {
            String input = text.getText();
            FeastDay selectedDay = databaseManager.selectFeastDay(input);
            /*displayedYear = Year.of(dateSelected.getYear());
            displayedMonth = dateSelected.getMonth();
            displayedDay = dateSelected;*/

            feastDayBox.getChildren().clear();
            feastDayBox.getChildren().add(manageFeastDayBox(selectedDay));
        });

        return searchButton;
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
