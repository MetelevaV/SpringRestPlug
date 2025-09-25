package org.example.springrestplug.repository;

import org.example.springrestplug.model.User;
import java.sql.*;

public class DataBaseWorker {
    private final String url = "jdbc:postgresql://192.168.0.164:5432/users";
    private final String user = "postgres";
    private final String password = "test";

    public User getUserByLogin(String login) {
        User result = null;
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
            String sql = "SELECT c.login, c.email, a.password, a.date " +
                    "FROM credentials c JOIN accounts a ON c.login = a.login " +
                    "WHERE c.login = '" + login + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                result = new User(rs.getString("login"), rs.getString("email"), rs.getString("password"), rs.getTimestamp("date").toLocalDateTime());
            }
        } catch (SQLException e) {
            System.err.println("Ошибка соединения: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ignored) {
            }
        }
        return result;
    }

    public int insertUser(User userObj) {
        String sqlCredentials = "INSERT INTO credentials(login, email) VALUES(?, ?)";
        String sqlAccounts;
        if (userObj.getDate() != null) {
            sqlAccounts = "INSERT INTO accounts(login, password, date) VALUES(?, ?, ?)";
        } else {
            sqlAccounts = "INSERT INTO accounts(login, password, date) VALUES(?, ?, DEFAULT)";
        }
        int rowsAffected = 0;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps1 = conn.prepareStatement(sqlCredentials);
             PreparedStatement ps2 = conn.prepareStatement(sqlAccounts)) {
            conn.setAutoCommit(false);

            ps1.setString(1, userObj.getLogin());
            ps1.setString(2, userObj.getEmail());
            ps1.executeUpdate();

            ps2.setString(1, userObj.getLogin());
            ps2.setString(2, userObj.getPassword());
            if (userObj.getDate() != null) {
                ps2.setTimestamp(3, Timestamp.valueOf(userObj.getDate()));
            }
            ps2.executeUpdate();
            rowsAffected += 1;

            conn.commit();
        } catch (SQLException e) {
            System.err.println("Ошибка: " + e.getMessage());
            rowsAffected = 0;
        }
        return rowsAffected;
    }

}

