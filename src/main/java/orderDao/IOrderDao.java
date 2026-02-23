package orderDao;

import model.Order;
import java.sql.SQLException;
import java.util.List;
import model.CartItem;

public interface IOrderDao {

    public void insertOrder(Order orderObj) throws SQLException;

    public Order getOrderById(int id);

    public List<Order> selectALlOrders();

    public boolean deleteOrder(int id) throws SQLException;

    public boolean updateOrder(Order orderObj) throws SQLException;

    public int createOrder(Order order);
    
    public void addOrderDetail(int orderId, int productId, int quanlity, Double price);
    
    public boolean saveFullOrder(Order order, List<CartItem> items) throws SQLException;
    
}
