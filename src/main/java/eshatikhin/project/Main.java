package eshatikhin.project;

import eshatikhin.project.dao.sql.CheckDAO;
import eshatikhin.project.dao.sql.ProductDAO;
import eshatikhin.project.dao.sql.UserDAO;
import eshatikhin.project.entities.Check;
import eshatikhin.project.entities.CheckProducts;
import eshatikhin.project.entities.Product;
import eshatikhin.project.entities.User;
import eshatikhin.project.entities.enums.CheckStatus;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static void CreateUser(UserDAO dao, User newUser) throws SQLException {
        dao.createUser(newUser);
        System.out.println("Successfully created new user '" + newUser.getUsername() + "'");
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
    private static void CreateProduct(ProductDAO dao, Product product) throws SQLException {
        dao.createProduct(product);
        System.out.println("Successfully created new product '" + product.getName() + "'");
    }
    private static void GetProduct(ProductDAO dao, int id) throws SQLException {
        Product prod = dao.getProduct(id);
        System.out.println(prod.getId() + " -- " + prod.getName() + " -- weight: " + prod.getWeight() +
                " kg -- price: " + prod.getPrice() + " hrn -- in stock: " + prod.getQty_instock());
    }
    private static void GetAllProducts(ProductDAO dao) throws SQLException {
        List<Product> products = dao.getProducts();
        for (Product prod : products) {
            System.out.println(prod.getId() + " -- " + prod.getName() + " -- weight: " + prod.getWeight() +
                    " kg -- price: " + prod.getPrice() + " hrn -- in stock: " + prod.getQty_instock());
        }
    }
    private static void CreateCheck(CheckDAO dao, Check check) throws SQLException {
        dao.createCheck(check);
        System.out.println("Successfully created new check at " + check.getTimestamp());
    }
    private static void AddProductToCheck(CheckDAO dao, int check_id, int product_id, float quantity) throws SQLException {
        dao.checkAddProduct(check_id, product_id, quantity);
        ProductDAO pdao = new ProductDAO();
        System.out.println("Successfully added product '" + pdao.getProduct(product_id).getName() + "' to check " + check_id);
    }
    private static void CloseCheck(CheckDAO dao, int check_id) throws SQLException {
        dao.closeCheck(check_id);
        Check check = dao.getCheck(check_id);
        System.out.println("Successfully closed check (id = " + check_id + ") for total = " + check.getCost() + " hrn");
        List<CheckProducts> checkproducts = dao.checkGetProducts(check_id);
        for (CheckProducts checkproduct : checkproducts) {
            Product prod = new ProductDAO().getProduct(checkproduct.getProduct_id());
            System.out.println(prod.getId() + " -- " + prod.getName() + " -- quantity: " + checkproduct.getQuantity() + " -- for " +
                    prod.getPrice() * checkproduct.getQuantity() + " hrn");
        }
    }
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        ProductDAO productDAO = new ProductDAO();
        CheckDAO checkDAO = new CheckDAO();
        try {
            //CreateUser(userDAO, new User("evgenius", "test", "Evgeniy Shatikhin", 4));
            //GetUserById(userDAO, 1);
            //GetUserByUsername(userDAO, "evgenius");
            //CreateProduct(productDAO, new Product(1, "coca-cola", 0.5F, 27.4, 15));
            //CreateProduct(productDAO, new Product(2, "pringles", 0.15F, 97.3, 27));
            GetAllProducts(productDAO);
            //GetProduct(productDAO, 2);
            //CreateCheck(checkDAO, new Check(null, CheckStatus.OPENED, 0, 1));
            //AddProductToCheck(checkDAO, 1, 1, 2);
            //AddProductToCheck(checkDAO, 1, 2, 3);
            //AddProductToCheck(checkDAO, 1, 1, 4);
            //CloseCheck(checkDAO, 1);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
