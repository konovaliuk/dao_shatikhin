package eshatikhin.project.dao;

import eshatikhin.project.dao.interfaces.ICheckDAO;
import eshatikhin.project.dao.interfaces.IProductDAO;
import eshatikhin.project.dao.interfaces.IRoleDAO;
import eshatikhin.project.dao.interfaces.IUserDAO;
import eshatikhin.project.dao.sql.CheckDAO;
import eshatikhin.project.dao.sql.ProductDAO;
import eshatikhin.project.dao.sql.RoleDAO;
import eshatikhin.project.dao.sql.UserDAO;

public class DAOFactory {
    public IUserDAO getUserDAO() {
        return new UserDAO();
    }
    public IRoleDAO getRoleDAO() { return new RoleDAO(); }
    public IProductDAO getProductDAO() { return new ProductDAO(); }
    public ICheckDAO getCheckDAO() { return new CheckDAO(); }
}
