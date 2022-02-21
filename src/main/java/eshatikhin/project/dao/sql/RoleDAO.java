package eshatikhin.project.dao.sql;

import eshatikhin.project.connection.ConnectionPool;
import eshatikhin.project.dao.interfaces.IRoleDAO;
import eshatikhin.project.entities.Role;
import eshatikhin.project.entities.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoleDAO implements IRoleDAO {
    private final int ROLEID = 1;
    private final int NAME = 2;
    @Override
    public Role getRole(int id) throws SQLException {
        final String SQL = "SELECT * FROM role WHERE id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ps.setInt(1, id);
            return GetRoleFromResultSet(ps.executeQuery());
        }
    }

    @Override
    public Role getRole(String name) throws SQLException {
        final String SQL = "SELECT * FROM role WHERE name = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ps.setString(1, name);
            return GetRoleFromResultSet(ps.executeQuery());
        }
    }
    @Override
    public Role getUserRole(User user) throws SQLException {
        final int id = user.getId();
        return getRole(id);
    }

    private Role GetRoleFromResultSet (ResultSet rs) {
        Role role = new Role();
        try {
            rs.next();
            role.setId(rs.getInt(ROLEID));
            role.setName(rs.getString(NAME));
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, e.getMessage());
            return null;
        }
        return role;
    }
}
