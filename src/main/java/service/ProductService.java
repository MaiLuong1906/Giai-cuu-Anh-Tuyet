package service;

import productDao.IProductDAO;
import productDao.ProductDAO;
import model.Product;
import java.sql.SQLException;
import java.util.List;

public class ProductService implements IProductService {

    private IProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    @Override
    public void addProduct(Product pro) throws SQLException {
        productDAO.insertProduct(pro);
    }

    @Override
    public Product getProductById(int id) {
        return productDAO.selectProduct(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productDAO.selectAllProducts();
    }

    @Override
    public boolean removeProduct(int id) throws SQLException {
        return productDAO.deleteProduct(id);
    }

    @Override
    public boolean modifyProduct(Product pro) throws SQLException {
        return productDAO.updateProduct(pro);
    }

    public List<Product> getProductsByPage(int offset, int limit) {
        return productDAO.selectProductsByPage(offset, limit);
    }

    public int getTotalProducts() {
        return productDAO.countTotalProducts();
    }

    @Override
    public boolean decreaseStock(int productId, int quantity) throws SQLException {
        return productDAO.decreaseStock(productId, quantity);
    }

}
