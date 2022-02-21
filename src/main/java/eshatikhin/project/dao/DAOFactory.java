package eshatikhin.project.dao;

import eshatikhin.project.dao.interfaces.IUserDAO;
import eshatikhin.project.dao.sql.UserDAO;

public class DAOFactory {
    public IUserDAO getUserDAO() {
        return new UserDAO();
    }
}
