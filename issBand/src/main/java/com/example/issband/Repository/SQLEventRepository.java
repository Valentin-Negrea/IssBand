package com.example.issband.Repository;

import com.example.issband.Domain.*;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SQLEventRepository extends MemoryRepository<Event> {
    private String dbLocation = "jdbc:sqlite:";
    private Connection conn;

    public SQLEventRepository(String dbLocation) {
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
        try (final Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS events(id int PRIMARY KEY, name varchar(200), date varchar(200), venue varchar(200), audience_id int, FOREIGN KEY (audience_id) REFERENCES audience(id));");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS event_bands(event_id int, band_id int, FOREIGN KEY (event_id) REFERENCES events(id), FOREIGN KEY (band_id) REFERENCES bands(id));");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tickets(id int PRIMARY KEY, event_id int, audience_id int, FOREIGN KEY (event_id) REFERENCES events(id), FOREIGN KEY (audience_id) REFERENCES audience(id));");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadData() {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM events");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                String dateString = rs.getString("date");

                Event event = new Event(
                        rs.getInt("id"),
                        rs.getString("name"),
                        dateString, // Pass the date string directly
                        rs.getString("venue"),
                        getBandsForEvent(rs.getInt("id")),
                        getTicketsForEvent(rs.getInt("id")),
                        getAudience(rs.getInt("audience_id"))
                );
                this.data.add(event);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





    public ArrayList<Band> getBandsForEvent(int eventId) throws SQLException {
        ArrayList<Band> bands = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM event_bands WHERE event_id = ?")) {
            statement.setInt(1, eventId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Band band = getBandById(rs.getInt("band_id"));
                if (band != null) {
                    bands.add(band);
                }
            }
        }
        return bands;
    }

    public ArrayList<Ticket> getTicketsForEvent(int eventId) throws SQLException {
        ArrayList<Ticket> tickets = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM tickets WHERE event_id = ?")) {
            statement.setInt(1, eventId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket(rs.getInt("id"), null, getAudience(rs.getInt("audience_id")));
                tickets.add(ticket);
            }
        }
        return tickets;
    }


    private Band getBandById(int bandId) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM bands WHERE id = ?")) {
            statement.setInt(1, bandId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Band(rs.getInt("id"), rs.getString("name"), getManager(rs.getInt("manager_id")));
            }
        }
        return null;
    }

    private Manager getManager(int managerId) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM managers WHERE id = ?")) {
            statement.setInt(1, managerId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Manager(rs.getInt("id"), rs.getString("name"));
            }
        }
        return null;
    }

    public Audience getAudience(int audienceId) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM audience WHERE id = ?")) {
            statement.setInt(1, audienceId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Audience(rs.getInt("id"), rs.getString("name"));
            }
        }
        return null;
    }

    public Event getEventById(int eventId) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM events WHERE id = ?")) {
            statement.setInt(1, eventId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                ArrayList<Ticket> tickets = getTicketsForEvent(eventId);

                return new Event(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("date"),
                        rs.getString("venue"),
                        getBandsForEvent(eventId),
                        tickets,
                        getAudience(rs.getInt("audience_id"))
                );
            }
        }
        return null;
    }

    public Ticket getTicketById(int ticketId) {
        Ticket ticket = null;
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM tickets WHERE id = ?")) {
            statement.setInt(1, ticketId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                // Retrieve the audience associated with the ticket
                Audience audience = getAudience(rs.getInt("audience_id"));
                // Create a new Ticket object
                ticket = new Ticket(rs.getInt("id"), null, audience);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ticket;
    }


    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();

        String query = "SELECT * FROM events";

        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String date = resultSet.getString("date");
                String venue = resultSet.getString("venue");
                // Populate other fields if necessary

                Event event = new Event(id, name, date, venue, null, null, null); // Replace nulls with appropriate values
                events.add(event);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return events;
    }

}
