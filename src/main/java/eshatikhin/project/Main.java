package eshatikhin.project;

import eshatikhin.project.dao.sql.UserDAO;
import eshatikhin.project.entities.User;

import java.sql.SQLException;

public class Main {
    private static void CreateUser(UserDAO dao, User newUser) throws SQLException {
        dao.createUser(newUser);
        System.out.println("Successfully created new user");
    }
    private static void GetUserById(UserDAO dao, int id) throws SQLException {
        User user = dao.getUser(id);
        if (user == null) System.out.println("No user found");
        else System.out.println("User with id = " + id + " is " + user.getUsername());
    }
    private static void GetUserByUsername(UserDAO dao, String username) throws SQLException {
        User user = dao.getUser(username);
        if (user == null) System.out.println("No user found");
        else System.out.println("User with username " + username + " has id = " + user.getId());
    }
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        try {
            //CreateUser(userDAO, new User("evgenius", "test", "Evgeniy Shatikhin", 4));
            GetUserById(userDAO, 1);
            GetUserByUsername(userDAO, "evgenius");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
