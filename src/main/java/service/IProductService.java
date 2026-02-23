package service;

import model.Product;
import java.sql.SQLException;
import java.util.List;

public interface IProductService {

    void addProduct(Product pro) throws SQLException;

    Product getProductById(int id);

    List<Product> getAllProducts();

    boolean removeProduct(int id) throws SQLException;

    boolean modifyProduct(Product pro) throws SQLException;

    boolean decreaseStock(int productId, int quantity) throws SQLException;

}
