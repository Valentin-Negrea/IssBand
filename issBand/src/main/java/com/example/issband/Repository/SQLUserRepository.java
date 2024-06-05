package com.example.issband.Repository;

import com.example.issband.Domain.User;
import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class SQLUserRepository extends MemoryRepository<User> {
    private String dbLocation;
    private Connection conn;

    public SQLUserRepository(String dbLocation) {
        this.dbLocation = "jdbc:sqlite:" + dbLocation;
        openConnection();
        createSchema();
        loadData();
    }

    private void openConnection() {
        try {
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(dbLocation);
            conn = ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error opening connection to database", e);
        }
    }

    private void createSchema() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY, " +
                "name VARCHAR(200) NOT NULL, " +
                "password VARCHAR(200) NOT NULL" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating schema", e);
        }
    }

    private void loadData() {
        String sql = "SELECT * FROM users";
        try (PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                int userId = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                User user = new User(userId, name, password);
                this.data.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading data", e);
        }
    }

    public void add(User user) {
        int newId = getNextId();
        user.setId(newId);

        String sql = "INSERT INTO users (id, name, password) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            this.data.add(user); // Add user to in-memory data
        } catch (SQLException e) {
            throw new RuntimeException("Error adding user", e);
        }
    }

    private int getNextId() {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM users";
        try (PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("next_id");
            } else {
                throw new RuntimeException("Error getting next ID");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting next ID", e);
        }
    }


    public boolean isUsernameUnique(String name) {
        String query = "SELECT COUNT(*) FROM users WHERE name = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    public void deleteUserByName(String name) {
        String sql = "DELETE FROM users WHERE name = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.executeUpdate();
            // Remove user from in-memory data
            this.data.removeIf(user -> user.getName().equals(name));
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user by name", e);
        }
    }


}
