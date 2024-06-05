package com.example.issband.Repository;

import com.example.issband.Domain.Audience;
import com.example.issband.Domain.Event;
import com.example.issband.Domain.Ticket;
import javafx.stage.Stage;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLTicketRepository extends MemoryRepository<Ticket> {
    private String dbLocation = "jdbc:sqlite:";
    private Connection conn;

    public SQLTicketRepository(String dbLocation) {
        this.dbLocation += dbLocation;
        openConnection();
        createSchema();
        loadData();
    }

    private void openConnection() {
        try {
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(dbLocation);
            if (conn == null || conn.isClosed())
                conn = ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createSchema() {
        // Assuming the tickets table has foreign key constraints to events and audience
        try (final Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tickets(id int, event_id int, audience_id int, FOREIGN KEY (event_id) REFERENCES events(id), FOREIGN KEY (audience_id) REFERENCES audience(id));");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadData() {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM tickets");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                int ticketId = rs.getInt("id");
                int eventId = rs.getInt("event_id");
                int audienceId = rs.getInt("audience_id");

                // Fetch event and audience objects
                Event event = getEventById(eventId);
                Audience audience = getAudienceById(audienceId);

                Ticket ticket = new Ticket(ticketId, event, audience);
                this.data.add(ticket);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Event getEventById(int eventId) throws SQLException {
        Ticket ticket = find(eventId);
        if (ticket != null) {
            return ticket.getEvent();
        } else {
            return null; // or throw an exception, depending on your requirements
        }
    }



    private Audience getAudienceById(int audienceId) throws SQLException {
        Ticket ticket = find(audienceId);
        if (ticket != null) {
            return ticket.getAudience();
        } else {
            return null; // or throw an exception, depending on your requirements
        }
    }


    public int getHighestTicketId() throws SQLException {
        String query = "SELECT MAX(id) FROM tickets";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public void addTicket(int id, int eventId, int audienceId) throws SQLException {
        String query = "INSERT INTO tickets (id, event_id, audience_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.setInt(2, eventId);
            stmt.setInt(3, audienceId);
            stmt.executeUpdate();
        }
    }


    public void deleteAudienceByName(String audienceName) throws SQLException {
        String deleteAudienceQuery = "DELETE FROM audience WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteAudienceQuery)) {
            pstmt.setString(1, audienceName);
            pstmt.executeUpdate();
        }
    }
    public void deleteTicketByAudienceNameAndEventId(int eventId, String audienceName) throws SQLException {
        String deleteTicketQuery = "DELETE FROM tickets WHERE event_id = ? AND audience_id = (SELECT id FROM audience WHERE name = ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteTicketQuery)) {
            pstmt.setInt(1, eventId);
            pstmt.setString(2, audienceName);
            pstmt.executeUpdate();
        }
    }
}
