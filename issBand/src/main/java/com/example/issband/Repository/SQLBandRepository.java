package com.example.issband.Repository;

import com.example.issband.Domain.Band;
import com.example.issband.Domain.Manager;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLBandRepository extends MemoryRepository<Band> {
    private String dbLocation = "jdbc:sqlite:";
    private Connection conn;

    public SQLBandRepository(String dbLocation) {
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
            // Create the Manager table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS managers(id INTEGER PRIMARY KEY, name VARCHAR(200));");
            // Create the Band table with a foreign key constraint to link it with the Manager table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bands(id INTEGER PRIMARY KEY, name VARCHAR(200), manager_id INTEGER, FOREIGN KEY (manager_id) REFERENCES managers(id));");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadData() {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM bands");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Band band = new Band(rs.getInt("id"), rs.getString("name"), getManager(rs.getInt("manager_id")));
                this.data.add(band);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Manager getManager(int managerId) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM managers WHERE id = ?")) {
            statement.setInt(1, managerId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Manager(rs.getInt("id"), rs.getString("name"));
            } else {
                return null;
            }
        }
    }

    public int getHighestBandId() throws SQLException {
        String query = "SELECT MAX(id) AS max_id FROM bands";
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("max_id");
            } else {
                return 0; // If no bands exist, return 0
            }
        }
    }

    public void add(Band band) {
        String query = "INSERT INTO bands (id, name, manager_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, band.getId());
            statement.setString(2, band.getName());
            statement.setInt(3, band.getManager().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addBandToEvent(int eventId, int bandId) throws SQLException {
        String query = "INSERT INTO event_bands (event_id, band_id) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, eventId);
            statement.setInt(2, bandId);
            statement.executeUpdate();
        }
    }

    public void deleteBandByName(String bandName) throws SQLException {
        String sql = "DELETE FROM bands WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bandName);
            pstmt.executeUpdate();
        }
    }

    public void deleteBandFromEvent(int eventId, int bandId) throws SQLException {
        String sql = "DELETE FROM event_bands WHERE event_id = ? AND band_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            pstmt.setInt(2, bandId);
            pstmt.executeUpdate();
        }
    }

    public Band getBandFromName(String bandName) {
        String sql = "SELECT * FROM bands WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bandName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int managerId = rs.getInt("manager_id");
                    // You may need to adjust the Band constructor parameters based on your Band class
                    return new Band(id, bandName, new Manager(managerId, null)); // Assuming you have a Manager class
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public List<Band> findBandsByEventId(int eventId) {
        List<Band> bands = new ArrayList<>();
        try {
            String query = "SELECT b.id AS band_id, b.name AS band_name, " +
                    "m.id AS manager_id, m.name AS manager_name " +
                    "FROM bands b " +
                    "JOIN event_bands eb ON b.id = eb.band_id " +
                    "JOIN managers m ON b.manager_id = m.id " +
                    "WHERE eb.event_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, eventId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int bandId = resultSet.getInt("band_id");
                String bandName = resultSet.getString("band_name");
                int managerId = resultSet.getInt("manager_id");
                String managerName = resultSet.getString("manager_name");
                Manager manager = new Manager(managerId, managerName);
                Band band = new Band(bandId, bandName, manager);
                bands.add(band);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bands;
    }
}
