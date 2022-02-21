package eshatikhin.project.dao.interfaces;

import eshatikhin.project.entities.Role;
import eshatikhin.project.entities.User;

import java.sql.SQLException;

public interface IRoleDAO {
    Role getRole(int id) throws SQLException;
    Role getRole(String name) throws SQLException;
    Role getUserRole(User user) throws SQLException;
}
