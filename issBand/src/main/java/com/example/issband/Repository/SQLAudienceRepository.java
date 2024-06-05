package com.example.issband.Repository;

import com.example.issband.Domain.Audience;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLAudienceRepository extends MemoryRepository<Audience> {
    private String dbLocation = "jdbc:sqlite:";
    private Connection conn;

    public SQLAudienceRepository(String dbLocation) {
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
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS audience(id int, name varchar(200));");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadData() {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM audience");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                int audienceId = rs.getInt("id");
                String audienceName = rs.getString("name");

                Audience audience = new Audience(audienceId, audienceName);
                this.data.add(audience);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Audience getAudienceByName(String name) throws SQLException {
        String query = "SELECT * FROM audience WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                return new Audience(id, name);
            }
        }
        return null;
    }

    public int getHighestAudienceId() throws SQLException {
        String query = "SELECT MAX(id) FROM audience";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public void add(Audience audience) {
        String query = "INSERT INTO audience (id, name) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, audience.getId());
            stmt.setString(2, audience.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
