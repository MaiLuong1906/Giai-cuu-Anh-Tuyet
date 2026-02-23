/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package productDao;

import dao.DBConnection;
import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProductDAO {

    private static final String INSERT_PRODUCT = "INSERT INTO Products (name, price, description, stock, import_date, status) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_PRODUCT_BY_ID = "SELECT * FROM Products WHERE id = ?";
    private static final String SELECT_ALL_PRODUCTS = "SELECT * FROM Products";
    private static final String DELETE_PRODUCT = "UPDATE Products SET status = ? WHERE id = ?";
    private static final String UPDATE_PRODUCT = "UPDATE Products SET name = ?, price = ?, description = ?, stock = ?, import_date = ?, status = ? WHERE id = ?";
    private static final String query = "SELECT * FROM Products WHERE status = 1 ORDER BY id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    @Override
    public void insertProduct(Product product) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setInt(4, product.getStock());
            preparedStatement.setDate(5, new java.sql.Date(product.getImportDate().getTime()));
            preparedStatement.setBoolean(6, true);
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Product added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product selectProduct(int id) {
        Product product = null;
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                product = extractProductFromResultSet(rs);
            } else {
                System.out.println("Product not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public List<Product> selectAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public boolean deleteProduct(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT)) {
            statement.setBoolean(1, false);
            statement.setInt(2, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public boolean updateProduct(Product product) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setString(3, product.getDescription());
            statement.setInt(4, product.getStock());
            statement.setDate(5, new java.sql.Date(product.getImportDate().getTime()));
            statement.setBoolean(6, product.isStatus());
            statement.setInt(7, product.getId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        double price = rs.getDouble("price");
        String description = rs.getString("description");
        int stock = rs.getInt("stock");
        Date importDate = rs.getDate("import_date");
        boolean status = rs.getBoolean("status");
        return new Product(id, name, price, description, stock, importDate, status);
    }

    @Override
    public List<Product> selectProductsByPage(int offset, int limit) {
        List<Product> products = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ptm = con.prepareStatement(query)) {

            ptm.setInt(1, offset);
            ptm.setInt(2, limit);

            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getInt("stock"),
                        rs.getDate("import_date"),
                        rs.getBoolean("status"));
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public int countTotalProducts() {
        String query = "SELECT COUNT(*) FROM Products WHERE status = 1";
        try (Connection con = DBConnection.getConnection();
                Statement stm = con.createStatement()) {

            ResultSet rs = stm.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean decreaseStock(int productId, int quantity) throws SQLException {
        String sql = "UPDATE Products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, quantity);
            ptm.setInt(2, productId);
            ptm.setInt(3, quantity);
            return ptm.executeUpdate() > 0;
        }
    }
}
