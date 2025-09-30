package org.example.springrestplug.repository;

import org.example.springrestplug.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;


@Repository
public class DataBaseWorker {
    private final DataSource dataSource;

    @Autowired
    public DataBaseWorker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User getUserByLogin(String login) throws SQLException{
        User result = null;
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT c.login, c.email, a.password, a.date " +
                             "FROM credentials c JOIN accounts a ON c.login = a.login " +
                             "WHERE c.login = '" + login + "'")) {

            if (rs.next()) {
                result = new User(
                        rs.getString("login"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getTimestamp("date").toLocalDateTime()
                );
            }

        }
        return result;

    }

    public int insertUser(User userObj) throws SQLException{
        String sqlCredentials = "INSERT INTO credentials(login, email) VALUES(?, ?)";
        String sqlAccounts = (userObj.getDate() != null)
                ? "INSERT INTO accounts(login, password, date) VALUES(?, ?, ?)"
                : "INSERT INTO accounts(login, password, date) VALUES(?, ?, DEFAULT)";

        int rowsAffected = 0;

        try (Connection conn = dataSource.getConnection();
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

            conn.commit();
            rowsAffected = 1;

        }
        return rowsAffected;
    }

}

