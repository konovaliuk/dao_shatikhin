package eshatikhin.project.dao.interfaces;

import eshatikhin.project.entities.Check;
import eshatikhin.project.entities.Role;
import eshatikhin.project.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
    User getUser(int id) throws SQLException;
    User getUser(String username) throws SQLException;
    List<Check> getCashierChecks(User user) throws SQLException;
    void createUser(User user) throws SQLException;
}
