package main.java.core;

public class Celebration {
    private String title;
    private String color;
    private String rank;
    private double rankNum;
    private int feast_day_id;

    public Celebration(String title, String color, String rank, double rankNum, int feastDayID) {
        this.title = title;
        this.color = color;
        this.rank = rank;
        this.rankNum = rankNum;
        this.feast_day_id = feastDayID;
    }

    public String getTitle() {
        return title;
    }

    public String getColor() {
        return color;
    }

    public String getRank() {
        return rank;
    }

    public double getRankNum() {
        return rankNum;
    }

    public int getFeast_day_id() {
        return feast_day_id;
    }
}
