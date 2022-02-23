package eshatikhin.project;

import eshatikhin.project.dao.DAOFactory;
import eshatikhin.project.dao.sql.CheckDAO;
import eshatikhin.project.dao.sql.ProductDAO;
import eshatikhin.project.dao.sql.UserDAO;
import eshatikhin.project.entities.Check;
import eshatikhin.project.entities.CheckProduct;
import eshatikhin.project.entities.Product;
import eshatikhin.project.entities.User;
import eshatikhin.project.entities.enums.CheckStatus;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
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
    private static int GetUserByUsername(UserDAO dao, String username) throws SQLException {
        User user = dao.getUser(username);
        if (user == null) {
            System.out.println("No user found");
            return -1;
        }
        else System.out.println("User with username " + username + " has id = " + user.getId());
        return user.getId();
    }
    private static void CreateProduct(ProductDAO dao, Product product) throws SQLException {
        dao.createProduct(product);
        System.out.println("Successfully created new product '" + product.getName() + "'");
    }
    private static void GetProduct(ProductDAO dao, int id) throws SQLException {
        Product prod = dao.getProduct(id);
        System.out.println("--- OBTAINING PRODUCT ID = " + id + " ---");
        System.out.println(prod.getId() + " -- " + prod.getName() + " -- weight: " + prod.getWeight() +
                " kg -- price: " + prod.getPrice() + " hrn -- in stock: " + prod.getQty_instock());
    }
    private static void GetAllProducts(ProductDAO dao) throws SQLException {
        System.out.println("--- ALL PRODUCTS IN DATABASE ---");
        List<Product> products = dao.getProducts();
        for (Product prod : products) {
            System.out.println(prod.getId() + " -- " + prod.getName() + " -- weight: " + prod.getWeight() +
                    " kg -- price: " + prod.getPrice() + " hrn -- in stock: " + prod.getQty_instock());
        }
    }
    private static int CreateCheck(CheckDAO dao, Check check) throws SQLException {
        dao.createCheck(check);
        System.out.println("Successfully created new check at " + check.getTimestamp());
        return check.getId();
    }
    private static void AddProductToCheck(CheckDAO dao, int check_id, int product_id, float quantity) throws SQLException {
        dao.checkAddProduct(check_id, product_id, quantity);
        ProductDAO pdao = new ProductDAO();
        System.out.println("Successfully added product '" + pdao.getProduct(product_id).getName() + "' to check " + check_id);
    }
    private static void CloseCheck(CheckDAO dao, int check_id) throws SQLException {
        dao.closeCheck(check_id);
        Check check = dao.getCheck(check_id);
        System.out.println("Successfully closed check (id = " + check_id + ") for total = " + check.getCost() + " hrn with products:");
        List<CheckProduct> checkproducts = dao.checkGetProducts(check_id);
        for (CheckProduct checkproduct : checkproducts) {
            Product prod = new ProductDAO().getProduct(checkproduct.getProduct_id());
            System.out.println(prod.getId() + " -- " + prod.getName() + " -- quantity: " + checkproduct.getQuantity() + " -- for " +
                    prod.getPrice() * checkproduct.getQuantity() + " hrn");
        }
    }
    public static void main(String[] args) {
        DAOFactory dao = new DAOFactory();
        UserDAO userDAO = (UserDAO) dao.getUserDAO();
        ProductDAO productDAO = (ProductDAO) dao.getProductDAO();
        CheckDAO checkDAO = (CheckDAO) dao.getCheckDAO();
        try {
            CreateUser(userDAO, new User("ldebeers", "test3", "Lucius DeBeers", 1));
            int id = GetUserByUsername(userDAO, "sdowd");
            GetUserById(userDAO, id);
            CreateProduct(productDAO, new Product(1, "coca-cola", 0.5F, 27.4, 15));
            CreateProduct(productDAO, new Product(2, "pringles", 0.15F, 97.3, 27));
            GetAllProducts(productDAO);
            GetProduct(productDAO, 2);
            int check_id = CreateCheck(checkDAO, new Check(Timestamp.from(Instant.now()), CheckStatus.OPENED, 0, id));
            AddProductToCheck(checkDAO, check_id, 1, 2);
            AddProductToCheck(checkDAO, check_id, 2, 3);
            AddProductToCheck(checkDAO, check_id, 1, 4);
            AddProductToCheck(checkDAO, check_id, 1, -1);
            CloseCheck(checkDAO, check_id);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
