package orderDao;

import dao.DBConnection;
import model.CartItem;
import model.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao implements IOrderDao {

    private static final String INSERT_ORDER = "INSERT INTO Orders (user_id, total_price, status) VALUES (?, ?, ?)";
    private static final String SELECT_ORDER_BY_ID = "SELECT * FROM Orders WHERE id = ?";
    private static final String SELECT_ALL_ORDERS = "SELECT * FROM Orders";
    private static final String DELETE_ORDER = "DELETE FROM Orders WHERE id = ?";
    private static final String UPDATE_ORDER = "UPDATE Orders SET user_id = ?, total_price = ?, status = ? WHERE id = ?";
    private static final String SELECT_ORDERS_BY_USER_ID = "SELECT * FROM Orders WHERE user_id = ?";
    private static final String INSERT_ORDER_DETAIL = "INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
    private static final String GET_LAST_INSERT_ID = "SELECT SCOPE_IDENTITY()";

    @Override
    public void insertOrder(Order orderObj) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER)) {

            preparedStatement.setInt(1, orderObj.getUserId());
            preparedStatement.setDouble(2, orderObj.getTotalPrice());
            preparedStatement.setString(3, orderObj.getStatus());

            preparedStatement.executeUpdate();
            System.out.println("Thêm đơn hàng mới thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Order getOrderById(int id) {
        Order order = null;
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDER_BY_ID)) {

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                order = Order.fromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public List<Order> selectALlOrders() {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ORDERS)) {

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                orders.add(Order.fromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public boolean deleteOrder(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_ORDER)) {

            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public boolean updateOrder(Order orderObj) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER)) {

            statement.setInt(1, orderObj.getUserId());
            statement.setDouble(2, orderObj.getTotalPrice());
            statement.setString(3, orderObj.getStatus());
            statement.setInt(4, orderObj.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    @Override
    public int createOrder(Order order) {
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER,
                        Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, order.getUserId());
            preparedStatement.setDouble(2, order.getTotalPrice());
            preparedStatement.setString(3, order.getStatus());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void addOrderDetail(int orderId, int productId, int quantity, Double price) {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT_ORDER_DETAIL)) {

            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, price);

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean saveFullOrder(Order order, List<CartItem> items) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Chèn vào bảng Orders
            String sqlOrder = "INSERT INTO Orders (user_id, total_price, status) VALUES (?, ?, ?)";
            int orderId;
            try (PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setInt(1, order.getUserId());
                psOrder.setDouble(2, order.getTotalPrice());
                psOrder.setString(3, order.getStatus());
                psOrder.executeUpdate();

                try (ResultSet rs = psOrder.getGeneratedKeys()) {
                    orderId = (rs.next()) ? rs.getInt(1) : 0;
                }
            }

            // 2. Chèn vào bảng OrderDetails
            String sqlDetail = "INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {
                for (CartItem item : items) {
                    psDetail.setInt(1, orderId);
                    psDetail.setInt(2, item.getProduct().getId());
                    psDetail.setInt(3, item.getQuantity());
                    psDetail.setDouble(4, item.getProduct().getPrice());
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            if (conn != null)
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }
}