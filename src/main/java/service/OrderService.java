package service;

import orderDao.OrderDao;
import orderDao.IOrderDao;
import model.Order;
import model.CartItem;
import java.sql.SQLException;
import java.util.List;

public class OrderService implements IOrderService {
    private IOrderDao orderDao;

    public OrderService() {
        this.orderDao = new OrderDao();
    }
    
    @Override
    public void insertOrder(Order orderObj) throws SQLException {
        orderDao.insertOrder(orderObj);
    }
    
    @Override
    public Order getOrderById(int id) {
        return orderDao.getOrderById(id);
    }
    
    @Override
    public List<Order> selectALlOrders() {
        return orderDao.selectALlOrders();
    }
    
    @Override
    public boolean deleteOrder(int id) throws SQLException {
        return orderDao.deleteOrder(id);
    }
    
    @Override
    public boolean updateOrder(Order orderObj) throws SQLException {
        return orderDao.updateOrder(orderObj);
    }
    
    @Override
    public int createOrder(Order order) {
        return orderDao.createOrder(order);
    }
    
    @Override
    public void addOrderDetail(int orderId, int productId, int quantity, Double price) {
        orderDao.addOrderDetail(orderId, productId, quantity, price);
    }
    
    @Override
    public boolean saveFullOrder(Order order, List<CartItem> items) {
        try {
            return orderDao.saveFullOrder(order, items);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}