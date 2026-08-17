package core;

import java.time.LocalDate;
import java.util.List;
import core.Celebration;

public class FeastDay {
    private int id;
    private LocalDate date;
    private String season;
    private int seasonWeek;
    private List<Celebration> celebrations;
    private String weekday;

    public FeastDay(int id, LocalDate date, String season, int seasonWeek, List<Celebration> celebrations, String weekday) {
        this.id = id;
        this.date = date;
        this.season = season;
        this.seasonWeek = seasonWeek;
        this.celebrations = celebrations;
        this.weekday = weekday;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getSeason() {
        return season;
    }

    public int getSeasonWeek() {
        return seasonWeek;
    }

    public List<core.Celebration> getCelebrations() {
        return celebrations;
    }

    public String getWeekday() {
        return weekday;
    }
}
