package service;

import model.Order;
import model.CartItem;
import java.sql.SQLException;
import java.util.List;

public interface IOrderService {

    void insertOrder(Order orderObj) throws SQLException;
    
    Order getOrderById(int id);
    
    List<Order> selectALlOrders();
    
    boolean deleteOrder(int id) throws SQLException;
    
    boolean updateOrder(Order orderObj) throws SQLException;
    
    int createOrder(Order order);
    
    void addOrderDetail(int orderId, int productId, int quantity, Double price);
    
    boolean saveFullOrder(Order order, List<CartItem> items);
    
}