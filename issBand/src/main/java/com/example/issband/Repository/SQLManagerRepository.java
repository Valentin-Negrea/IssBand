package com.example.issband.Repository;

import com.example.issband.Domain.Manager;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLManagerRepository extends MemoryRepository<Manager> {
    private String dbLocation = "jdbc:sqlite:";
    private Connection conn;

    public SQLManagerRepository(String dbLocation) {
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
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS managers(id int, name varchar(200));");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadData() {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM managers");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                int managerId = rs.getInt("id");
                String managerName = rs.getString("name");

                Manager manager = new Manager(managerId, managerName);
                this.data.add(manager);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Manager getManagerByName(String name) throws SQLException {
        String sql = "SELECT * FROM managers WHERE name = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Manager(rs.getInt("id"), rs.getString("name"));
            }
            return null;
        }
    }
}
