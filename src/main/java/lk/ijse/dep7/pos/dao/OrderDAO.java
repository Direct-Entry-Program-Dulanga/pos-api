package lk.ijse.dep7.pos.dao;

import lk.ijse.dep7.pos.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDAO {

    void saveOrder(Order order) throws Exception;

    void updateOrder(Order order) throws Exception;

    void deleteOrderById(String orderId) throws Exception;

    Optional<Order> findOrderById(String orderId) throws Exception;

    List<Order> findAllOrders() throws Exception;

    long countOrders() throws Exception;

    boolean existsOrderById(String orderId) throws Exception;

    String getLastOrderId() throws Exception;
}
