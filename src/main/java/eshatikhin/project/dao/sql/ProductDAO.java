package eshatikhin.project.dao.sql;

import eshatikhin.project.connection.ConnectionPool;
import eshatikhin.project.dao.interfaces.IProductDAO;
import eshatikhin.project.entities.Product;
import eshatikhin.project.entities.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDAO implements IProductDAO {
    private final int PRODUCTID = 1;
    private final int NAME = 2;
    private final int WEIGHT = 3;
    private final int PRICE = 4;
    private final int QTY = 5;
    @Override
    public Product getProduct(int id) throws SQLException {
        final String SQL = "SELECT * FROM product WHERE id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ps.setInt(1, id);
            return GetProductFromResultSet(ps.executeQuery());
        }
    }

    @Override
    public Product getProduct(String name) throws SQLException {
        final String SQL = "SELECT * FROM product WHERE name = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ps.setString(1, name);
            return GetProductFromResultSet(ps.executeQuery());
        }
    }

    @Override
    public void createProduct(Product product) throws SQLException {
        final String SQL = "INSERT INTO product (id, name, weight, price, qty_instock) VALUES (?, ?, ?, ?, ?);";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ps.setInt(PRODUCTID, product.getId());
            ps.setString(NAME, product.getName());
            ps.setFloat(WEIGHT, product.getWeight());
            ps.setDouble(PRICE, product.getPrice());
            ps.setFloat(QTY, product.getQty_instock());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Product> getProducts() throws SQLException {
        final String SQL = "SELECT * FROM product";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ResultSet result = ps.executeQuery();
            List<Product> products = new ArrayList<Product>();
            while(result.next()) {
                products.add(GetProductFromResultSet(result));
            }
            return products;
        }
    }

    private Product GetProductFromResultSet (ResultSet rs) {
        Product product = new Product();
        try {
            if (rs.isBeforeFirst()) rs.next();
            product.setId(rs.getInt(PRODUCTID));
            product.setName(rs.getString(NAME));
            product.setWeight(rs.getFloat(WEIGHT));
            product.setPrice(rs.getDouble(PRICE));
            product.setQty_instock(rs.getFloat(QTY));

        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, e.getMessage());
            return null;
        }
        return product;
    }
}
