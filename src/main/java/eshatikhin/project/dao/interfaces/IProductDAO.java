package eshatikhin.project.dao.interfaces;

import eshatikhin.project.entities.Product;

import java.sql.SQLException;
import java.util.List;

public interface IProductDAO {
    Product getProduct(int id) throws SQLException;
    Product getProduct(String name) throws SQLException;
    void createProduct(Product product) throws SQLException;
    List<Product> getProducts() throws SQLException;
}
