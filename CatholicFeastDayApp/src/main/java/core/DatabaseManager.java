package main.java.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:feastdays.db";

    public Connection connect() throws SQLException{
        return DriverManager.getConnection(URL);
    }

    public void createTable(String query) {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertFeastDays(FeastDay feastDay) {
    }

    public FeastDay selectFeastDay() {
    }
}
