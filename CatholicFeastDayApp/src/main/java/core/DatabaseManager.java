package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:feastdays.db";

    public Connection connect() throws SQLException{
        return DriverManager.getConnection(URL);
    }

    public void runQuery(String query) {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertFeastDays(FeastDay feastDay) {
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO feast_days (id, date, season, season_week, weekday)" +
                "VALUES(?, ?, ?, ?, ?)")) {
            stmt.setInt(1, feastDay.getId());
            stmt.setString(2, String.valueOf(feastDay.getDate()));
            stmt.setString(3, feastDay.getSeason());
            stmt.setInt(4, feastDay.getSeasonWeek());
            stmt.setString(5, feastDay.getWeekday());

            stmt.executeUpdate();

            List<Celebration> celebrations = feastDay.getCelebrations();
            for (Celebration celebration : celebrations) {
                insertCelebrations(celebration);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertCelebrations(Celebration celebration) {
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO celebrations (title, color, rank, rank_num, feast_day_id)" +
                "VALUES(?, ?, ?, ?, ?)")) {
            stmt.setString(1, celebration.getTitle());
            stmt.setString(2, celebration.getColor());
            stmt.setString(3, celebration.getRank());
            stmt.setDouble(4, celebration.getRankNum());
            stmt.setInt(5, celebration.getFeast_day_id());

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public FeastDay selectFeastDay(String date) {
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM feast_days WHERE date = ?")) {
            stmt.setString(1, date);
            int id = 0;
            LocalDate dayDate;
            String season = "";
            int seasonWeek = 0;
            List<Celebration> celebrations = new ArrayList<>();
            String weekday = "";
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
                dayDate = LocalDate.parse(rs.getString("date"));
                season = rs.getString("season");
                seasonWeek = rs.getInt("season_week");
                weekday = rs.getString("weekday");

                String title = "";
                String color = "";
                String rank = "";
                double rank_num = 0.0;

                PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM celebrations WHERE feast_day_id = ?");
                stmt2.setInt(1, id);
                ResultSet rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    title = rs2.getString("title");
                    color = rs2.getString("color");
                    rank = rs2.getString("rank");
                    rank_num = rs2.getDouble("rank_num");

                    celebrations.add(new Celebration(title, color, rank, rank_num, id));
                }

                return new FeastDay(id, dayDate, season, seasonWeek, celebrations, weekday);
            }
            else {
                return null;

            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasFeastDays() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1 FROM feast_days LIMIT 1")) {
            return rs.next();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
