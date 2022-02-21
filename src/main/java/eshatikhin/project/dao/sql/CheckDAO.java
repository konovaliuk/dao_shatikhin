package eshatikhin.project.dao.sql;

import eshatikhin.project.connection.ConnectionPool;
import eshatikhin.project.dao.interfaces.ICheckDAO;
import eshatikhin.project.entities.Check;
import eshatikhin.project.entities.CheckProducts;
import eshatikhin.project.entities.Product;
import eshatikhin.project.entities.enums.CheckStatus;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckDAO implements ICheckDAO {
    private final int CHECKID = 1;
    private final int TIMESTAMP = 2;
    private final int STATUS = 3;
    private final int COST = 4;
    private final int CASHIERID = 5;
    private final int CHECKPRODUCT_CHECKID = 1;
    private final int CHECKPRODUCT_PRODUCTID = 2;
    private final int CHECKPRODUCT_QUANTITY = 3;
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
    public void checkAddProduct(int check_id, int product_id, float quantity) throws SQLException {
        String SQL = "SELECT status FROM `check` WHERE id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ps.setInt(1, check_id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (!Objects.equals(rs.getString(1), CheckStatus.OPENED.name())) {
                throw new SQLException("Check " + check_id + " is not opened");
            }
        }
        SQL = "SELECT * FROM check_product WHERE check_id = ? AND product_id = ?";
        float baseQuantity = 0;
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ps.setInt(1, check_id);
            ps.setInt(2, product_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                baseQuantity = rs.getFloat(CHECKPRODUCT_QUANTITY);
            }
        }
        if (baseQuantity == 0) {
            SQL = "INSERT INTO check_product (check_id, product_id, quantity) VALUES (?, ?, ?)";
            try (Connection connection = ConnectionPool.getConnection();
                 PreparedStatement ps = connection.prepareStatement(SQL)
            ) {
                ps.setInt(CHECKPRODUCT_CHECKID, check_id);
                ps.setInt(CHECKPRODUCT_PRODUCTID, product_id);
                ps.setFloat(CHECKPRODUCT_QUANTITY, quantity);
                ps.executeUpdate();
            }
        }
        else {
            SQL = "UPDATE check_product SET quantity = ? WHERE check_id = ? AND product_id = ?";
            try (Connection connection = ConnectionPool.getConnection();
                 PreparedStatement ps = connection.prepareStatement(SQL)
            ) {
                ps.setFloat(1, baseQuantity + quantity);
                ps.setInt(2, check_id);
                ps.setInt(3, product_id);
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void closeCheck(int id) throws SQLException {
        final String SQL = "UPDATE `check` SET status = '" + CheckStatus.CLOSED.name() + "', cost = ? WHERE id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            List<CheckProducts> products = checkGetProducts(id);
            double total = 0;
            for (CheckProducts checkproduct : products) {
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
    public List<CheckProducts> checkGetProducts(int id) throws SQLException {
        final String SQL = "SELECT product_id, quantity FROM check_product WHERE check_id = ?";
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            List<CheckProducts> products = new ArrayList<CheckProducts>();
            while(result.next()) {
                products.add(new CheckProducts(id, result.getInt(1), result.getFloat(2)));
            }
            return products;
        }
    }
    private Check GetCheckFromResultSet (ResultSet rs) {
        Check check = new Check();
        try {
            if (rs.isBeforeFirst()) rs.next();
            //rs.next();
            check.setId(rs.getInt(CHECKID));
            check.setTimestamp(rs.getTimestamp(TIMESTAMP));
            check.setStatus(CheckStatus.valueOf(rs.getString(STATUS)));
            check.setCost(rs.getDouble(COST));
            check.setCashier_id(rs.getInt(CASHIERID));

        } catch (SQLException e) {
            Logger.getLogger(CheckDAO.class.getName()).log(Level.SEVERE, e.getMessage());
            return null;
        }
        return check;
    }
}
