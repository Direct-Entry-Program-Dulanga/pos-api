package lk.ijse.dep7.pos.dao;

import lk.ijse.dep7.pos.dto.OrderDTO;
import lk.ijse.dep7.pos.entity.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    public Order findOrderById(String orderId) throws Exception {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM `order` WHERE id=?");
        stm.setString(1, orderId);
        ResultSet rst = stm.executeQuery();

        if (rst.next()) {
            return new Order(orderId, rst.getDate("date"), rst.getString("customer_id"));
        } else {
            throw new RuntimeException(orderId + " is not found");
        }
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


//    public void saveOrderDetail(String orderId, OrderDetailDTO orderDetail) {
//        try {
//            PreparedStatement stm = connection.prepareStatement("INSERT INTO order_detail (order_id, item_code, unit_price, qty) VALUES (?,?,?,?)");
//            stm.setString(1, orderId);
//            stm.setString(2, orderDetail.getItemCode());
//            stm.setBigDecimal(3, orderDetail.getUnitPrice());
//            stm.setInt(4, orderDetail.getQty());
//            stm.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to save the order detail", e);
//        }
//    }
//
//    public List<OrderDTO> searchOrders(String query) {
//        List<OrderDTO> orderList = new ArrayList<>();
//
//        try {
//            String[] searchWords = query.split("\\s");
//            StringBuilder sqlBuilder = new StringBuilder("SELECT o.*, c.name, order_total.total\n" +
//                    "FROM `order` o\n" +
//                    "         INNER JOIN customer c on o.customer_id = c.id\n" +
//                    "         INNER JOIN\n" +
//                    "     (SELECT order_id, SUM(qty * unit_price) AS total FROM order_detail od GROUP BY order_id) AS order_total\n" +
//                    "     ON o.id = order_total.order_id\n" +
//                    "WHERE (order_id LIKE ?\n" +
//                    "    OR date LIKE ?\n" +
//                    "    OR customer_id LIKE ?\n" +
//                    "    OR name LIKE ?) ");
//
//            for (int i = 1; i < searchWords.length; i++) {
//                sqlBuilder.append("AND (\n" +
//                        "            order_id LIKE ?\n" +
//                        "        OR date LIKE ?\n" +
//                        "        OR customer_id LIKE ?\n" +
//                        "        OR name LIKE ?)");
//            }
//            PreparedStatement stm = connection.prepareStatement(sqlBuilder.toString());
//
//            for (int i = 0; i < searchWords.length * 4; i++) {
//                stm.setString(i + 1, "%" + searchWords[(i / 4)] + "%");
//            }
//            ResultSet rst = stm.executeQuery();
//
//            while (rst.next()) {
//                orderList.add(new OrderDTO(rst.getString("id"), rst.getDate("date").toLocalDate(),
//                        rst.getString("customer_id"), rst.getString("name"), rst.getBigDecimal("total")));
//            }
//
//            return orderList;
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to search orders", e);
//        }
//    }
//
//    public long getSearchOrderCount(String query) {
//        try {
//            String[] searchWords = query.split("\\s");
//            StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(*) \n" +
//                    "FROM `order` o\n" +
//                    "         INNER JOIN customer c on o.customer_id = c.id\n" +
//                    "         INNER JOIN\n" +
//                    "     (SELECT order_id, SUM(qty * unit_price) AS total FROM order_detail od GROUP BY order_id) AS order_total\n" +
//                    "     ON o.id = order_total.order_id\n" +
//                    "WHERE (order_id LIKE ?\n" +
//                    "    OR date LIKE ?\n" +
//                    "    OR customer_id LIKE ?\n" +
//                    "    OR name LIKE ?) ");
//
//            for (int i = 1; i < searchWords.length; i++) {
//                sqlBuilder.append("AND (\n" +
//                        "            order_id LIKE ?\n" +
//                        "        OR date LIKE ?\n" +
//                        "        OR customer_id LIKE ?\n" +
//                        "        OR name LIKE ?)");
//            }
//            PreparedStatement stm = connection.prepareStatement(sqlBuilder.toString());
//
//            for (int i = 0; i < searchWords.length * 4; i++) {
//                stm.setString(i + 1, "%" + searchWords[(i / 4)] + "%");
//            }
//
//            ResultSet rst = stm.executeQuery();
//            rst.next();
//            return rst.getLong(1);
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to get the count", e);
//        }
//    }
//
//    public List<OrderDTO> searchOrders(String query, int page, int size) {
//        List<OrderDTO> orderList = new ArrayList<>();
//
//        try {
//            String[] searchWords = query.split("\\s");
//            StringBuilder sqlBuilder = new StringBuilder("SELECT o.*, c.name, order_total.total\n" +
//                    "FROM `order` o\n" +
//                    "         INNER JOIN customer c on o.customer_id = c.id\n" +
//                    "         INNER JOIN\n" +
//                    "     (SELECT order_id, SUM(qty * unit_price) AS total FROM order_detail od GROUP BY order_id) AS order_total\n" +
//                    "     ON o.id = order_total.order_id\n" +
//                    "WHERE (order_id LIKE ?\n" +
//                    "    OR date LIKE ?\n" +
//                    "    OR customer_id LIKE ?\n" +
//                    "    OR name LIKE ?) ");
//
//            for (int i = 1; i < searchWords.length; i++) {
//                sqlBuilder.append("AND (\n" +
//                        "            order_id LIKE ?\n" +
//                        "        OR date LIKE ?\n" +
//                        "        OR customer_id LIKE ?\n" +
//                        "        OR name LIKE ?)");
//            }
//            sqlBuilder.append(" LIMIT ? OFFSET ?");
//            PreparedStatement stm = connection.prepareStatement(sqlBuilder.toString());
//
//            for (int i = 0; i < searchWords.length * 4; i++) {
//                stm.setString(i + 1, "%" + searchWords[(i / 4)] + "%");
//            }
//            stm.setInt((searchWords.length * 4) + 1, size);
//            stm.setInt((searchWords.length * 4) + 2, size * (page - 1));
//            ResultSet rst = stm.executeQuery();
//
//            while (rst.next()) {
//                orderList.add(new OrderDTO(rst.getString("id"), rst.getDate("date").toLocalDate(),
//                        rst.getString("customer_id"), rst.getString("name"), rst.getBigDecimal("total")));
//            }
//
//            return orderList;
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to search orders", e);
//        }
//    }
//
//    public List<OrderDetailDTO> findOrderDetails(String orderId) {
//        List<OrderDetailDTO> orderDetailsList = new ArrayList<>();
//
//        try {
//            PreparedStatement stm = connection.prepareStatement("SELECT id FROM `order` WHERE id=?");
//            stm.setString(1, orderId);
//
//            if (!stm.executeQuery().next()) throw new RuntimeException("Invalid order id");
//
//            stm = connection.prepareStatement("SELECT * FROM order_detail WHERE order_id=?");
//            stm.setString(1, orderId);
//            ResultSet rst = stm.executeQuery();
//
//            while (rst.next()) {
//                orderDetailsList.add(new OrderDetailDTO(rst.getString("item_code"),
//                        rst.getInt("qty"),
//                        rst.getBigDecimal("unit_price")));
//            }
//
//            return orderDetailsList;
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed fetch order details for order id: " + orderId, e);
//        }
//    }
//
//    public String getLastOrderId() {
//        try {
//            ResultSet rst = connection.createStatement().executeQuery("SELECT id FROM `order` ORDER BY id DESC LIMIT 1;");
//
//            if (rst.next()) {
//                return rst.getString("id");
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Failed operation");
//        }
//    }
}
