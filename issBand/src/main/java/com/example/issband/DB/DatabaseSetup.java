package com.example.issband.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {
    public static void main(String[] args) {
        String dbLocation = "jdbc:sqlite:iss.db";
        try (Connection conn = DriverManager.getConnection(dbLocation)) {
            // Insert sample managers
            try (Statement stmt = conn.createStatement()) {

//                stmt.executeUpdate("DELETE FROM users;");
                stmt.executeUpdate("DELETE FROM users WHERE name NOT IN ('User One', 'User Two');");
//                stmt.executeUpdate("INSERT INTO users (id, name, password) VALUES (1, 'User One', 'passwordOne'), (2, 'User Two', 'passwordTwo');");
                stmt.executeUpdate("INSERT INTO managers (id, name) VALUES (1, 'Manager One'), (2, 'Manager Two');");

                // Insert sample bands
                stmt.executeUpdate("INSERT INTO bands (id, name, manager_id) VALUES (1, 'Band One', 1), (2, 'Band Two', 2);");

                // Insert sample audience
                stmt.executeUpdate("INSERT INTO audience (id, name) VALUES (1, 'Audience One'), (2, 'Audience Two');");

                // Insert sample events
                stmt.executeUpdate("INSERT INTO events (id, name, date, venue, audience_id) VALUES (1, 'Event One', '2024-06-15', 'Venue One', 1), (2, 'Event Two', '2024-07-20', 'Venue Two', 2);");

                // Insert sample event bands
                stmt.executeUpdate("INSERT INTO event_bands (event_id, band_id) VALUES (1, 1), (1, 2), (2, 1);");

                // Insert sample tickets
                stmt.executeUpdate("INSERT INTO tickets (id, event_id, audience_id) VALUES (1, 1, 1), (2, 1, 2), (3, 2, 1);");

                ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

