package eshatikhin.project.dao.sql;

import eshatikhin.project.connection.ConnectionPool;
import eshatikhin.project.dao.interfaces.ICheckDAO;
import eshatikhin.project.entities.Check;
import eshatikhin.project.entities.CheckProduct;
import eshatikhin.project.entities.Product;
import eshatikhin.project.entities.enums.CheckStatus;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckDAO implements ICheckDAO {
    @Override
    public Check getCheck(int id) throws SQLException {
        final String SQL = "SELECT * FROM `check` WHERE id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ps.setInt(1, id);
            return GetCheckFromResultSet(ps.executeQuery());
        }
    }

    @Override
    public CheckProduct getCheckProduct(int check_id, int product_id) throws SQLException {
        String SQL = "SELECT * FROM check_product WHERE check_id = ? AND product_id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ps.setInt(1, check_id);
            ps.setInt(2, product_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CheckProduct(check_id, product_id, rs.getFloat(3));
            }
            else return null;
        }
    }

    @Override
    public void createCheck(Check check) throws SQLException {
        final String SQL = "INSERT INTO `check` (timestamp, status, cost, cashier_id) VALUES (?, ?, ?, ?);";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            if (check.getTimestamp() == null) ps.setTimestamp(1, Timestamp.from(Instant.now()));
            else ps.setTimestamp(1, check.getTimestamp());
            ps.setString(2, check.getStatus().name());
            ps.setDouble(3, check.getCost());
            ps.setInt(4, check.getCashier_id());
            ps.executeUpdate();
        }
    }

    @Override
    // quantity can be negative -- that means decreasing amount of product in check
    public void checkAddProduct(int check_id, int product_id, float quantity) throws SQLException {
        if (GetCheckStatus(check_id) != CheckStatus.OPENED) {
            throw new SQLException("Check " + check_id + " is closed");
        }
        float baseQuantity = 0;
        CheckProduct checkProduct = getCheckProduct(check_id, product_id);
        if (checkProduct != null) baseQuantity = checkProduct.getQuantity();
        if (baseQuantity == 0) {
            if (quantity < 0) throw new SQLException("Can't add negative amount of products to check");
            String SQL = "INSERT INTO check_product (check_id, product_id, quantity) VALUES (?, ?, ?)";
            try (Connection connection = ConnectionPool.getConnection();
                 PreparedStatement ps = connection.prepareStatement(SQL)
            ) {
                ps.setInt(1, check_id);
                ps.setInt(2, product_id);
                ps.setFloat(3, quantity);
                ps.executeUpdate();
            }
        }
        else {
            float amount = baseQuantity + quantity;
            if (amount < 0) amount = 0;
            String SQL = "UPDATE check_product SET quantity = ? WHERE check_id = ? AND product_id = ?";
            try (Connection connection = ConnectionPool.getConnection();
                 PreparedStatement ps = connection.prepareStatement(SQL)
            ) {
                ps.setFloat(1, amount);
                ps.setInt(2, check_id);
                ps.setInt(3, product_id);
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void checkRemoveProduct(int check_id, int product_id) throws SQLException {
        if (GetCheckStatus(check_id) != CheckStatus.OPENED) {
            throw new SQLException("Check " + check_id + " is closed");
        }
        String SQL = "DELETE FROM check_product WHERE check_id = ? AND product_id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ps.setInt(1, check_id);
            ps.setInt(2, product_id);
            ps.executeUpdate();
        }
    }

    @Override
    public void closeCheck(int id) throws SQLException {
        final String SQL = "UPDATE `check` SET status = '" + CheckStatus.CLOSED.name() + "', cost = ? WHERE id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            List<CheckProduct> products = checkGetProducts(id);
            double total = 0;
            for (CheckProduct checkproduct : products) {
                ProductDAO dao = new ProductDAO();
                Product product = dao.getProduct(checkproduct.getProduct_id());
                total += product.getPrice() * checkproduct.getQuantity();
            }
            ps.setDouble(1, total);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<CheckProduct> checkGetProducts(int id) throws SQLException {
        final String SQL = "SELECT product_id, quantity FROM check_product WHERE check_id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            List<CheckProduct> products = new ArrayList<CheckProduct>();
            while(result.next()) {
                products.add(new CheckProduct(id, result.getInt(1), result.getFloat(2)));
            }
            return products;
        }
    }
    private CheckStatus GetCheckStatus(int check_id) throws SQLException {
        return getCheck(check_id).getStatus();
    }
    private Check GetCheckFromResultSet (ResultSet rs) {
        Check check = new Check();
        try {
            if (rs.isBeforeFirst()) rs.next();
            //rs.next();
            check.setId(rs.getInt(1));
            check.setTimestamp(rs.getTimestamp(2));
            check.setStatus(CheckStatus.valueOf(rs.getString(3)));
            check.setCost(rs.getDouble(4));
            check.setCashier_id(rs.getInt(5));

        } catch (SQLException e) {
            Logger.getLogger(CheckDAO.class.getName()).log(Level.SEVERE, e.getMessage());
            return null;
        }
        return check;
    }
}
