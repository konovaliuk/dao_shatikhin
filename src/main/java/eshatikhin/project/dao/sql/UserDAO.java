package eshatikhin.project.dao.sql;

import eshatikhin.project.connection.ConnectionPool;
import eshatikhin.project.dao.interfaces.IUserDAO;
import eshatikhin.project.entities.Check;
import eshatikhin.project.entities.Role;
import eshatikhin.project.entities.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO implements IUserDAO {
    @Override
    public User getUser(int id) throws SQLException {
        final String SQL = "SELECT * FROM user WHERE id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ps.setInt(1, id);
            return GetUserFromResultSet(ps.executeQuery());
        }
    }
    @Override
    public User getUser(String username) throws SQLException {
        final String SQL = "SELECT * FROM user WHERE username = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ps.setString(1, username);
            return GetUserFromResultSet(ps.executeQuery());
        }
    }
    @Override
    public void createUser(User user) throws SQLException {
        final String SQL = "INSERT INTO user (username, password, full_name, role_id) VALUES (?, ?, ?, ?);";
        try (Connection connection = ConnectionPool.getConnection();
        PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ps.setString(1, user.getUsername());
            final String password_hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(11));
            ps.setString(2, password_hash);
            ps.setString(3, user.getFull_name());
            ps.setInt(4, user.getRole_id());
            ps.executeUpdate();
        }
    }
    private User GetUserFromResultSet (ResultSet rs) {
        User user = new User();
        try {
            if (rs.isBeforeFirst()) rs.next();
            user.setId(rs.getInt(1));
            user.setUsername(rs.getString(2));
            user.setPassword(rs.getString(3));
            user.setFull_name(rs.getString(4));
            user.setRole_id(rs.getInt(5));
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, e.getMessage());
            return null;
        }
        return user;
    }
}
