package eshatikhin.project.dao.interfaces;

import eshatikhin.project.entities.Check;
import eshatikhin.project.entities.CheckProduct;

import java.sql.SQLException;
import java.util.List;

public interface ICheckDAO {
    Check getCheck(int id) throws SQLException;
    CheckProduct getCheckProduct(int check_id, int product_id) throws SQLException;
    void createCheck(Check check) throws SQLException;
    void checkAddProduct(int check_id, int product_id, float quantity) throws SQLException;
    void checkRemoveProduct(int check_id, int product_id) throws SQLException;
    void closeCheck(int id) throws SQLException;
    List<CheckProduct> checkGetProducts(int id) throws SQLException;
}
