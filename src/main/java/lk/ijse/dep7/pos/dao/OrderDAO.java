package lk.ijse.dep7.pos.dao;

import lk.ijse.dep7.pos.entity.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAO {

    private final Connection connection;

    public OrderDAO(Connection connection) {
        this.connection = connection;
    }

    public void saveOrder(Order order) throws Exception {
        PreparedStatement stm = connection.prepareStatement("INSERT INTO `order` (id, date, customer_id) VALUES (?,?,?)");
        stm.setString(1, order.getId());
        stm.setDate(2, order.getDate());
        stm.setString(3, order.getCustomerId());
        stm.executeUpdate();
    }

    public void updateOrder(Order order) throws Exception {
        PreparedStatement stm = connection.prepareStatement("UPDATE `order` SET date=?, customer_id=? WHERE id=?");
        stm.setDate(1, order.getDate());
        stm.setString(2, order.getCustomerId());
        stm.setString(3, order.getId());
        stm.executeUpdate();
    }

    public void deleteOrderById(String orderId) throws Exception {
        PreparedStatement stm = connection.prepareStatement("DELETE FROM `order` WHERE id=?");
        stm.setString(1, orderId);
        stm.executeUpdate();
    }

    public Optional<Order> findOrderById(String orderId) throws Exception {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM `order` WHERE id=?");
        stm.setString(1, orderId);
        ResultSet rst = stm.executeQuery();
        return (rst.next()) ? Optional.of(new Order(orderId, rst.getDate("date"), rst.getString("customer_id"))) : Optional.empty();
    }

    public List<Order> findAllOrders() throws Exception {
        List<Order> orderList = new ArrayList<>();

        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM `order`");

        while (rst.next()) {
            orderList.add(new Order(rst.getString("id"), rst.getDate("date"), rst.getString("customer_id")));
        }

        return orderList;
    }

    public long countOrders() throws Exception {
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT COUNT(*) FROM `order`");
        rst.next();
        return rst.getLong(1);
    }

    public boolean existsOrderById(String orderId) throws Exception {
        PreparedStatement stm = connection.prepareStatement("SELECT id FROM `order` WHERE id=?");
        stm.setString(1, orderId);
        return stm.executeQuery().next();
    }


    public String getLastOrderId() throws Exception {
        ResultSet rst = connection.createStatement().executeQuery("SELECT id FROM `order` ORDER BY id DESC LIMIT 1;");
        return rst.next() ? rst.getString("id") : null;
    }
}
