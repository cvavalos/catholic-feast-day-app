package main.java.core;

public class Celebration {
    private String title;
    private String color;
    private String rank;
    private double rankNum;

    public Celebration(String title, String color, String rank, double rankNum) {
        this.title = title;
        this.color = color;
        this.rank = rank;
        this.rankNum = rankNum;
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
}
