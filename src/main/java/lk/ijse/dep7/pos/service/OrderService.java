package lk.ijse.dep7.pos.service;

import lk.ijse.dep7.pos.dao.impl.CustomerDAOImpl;
import lk.ijse.dep7.pos.dao.impl.OrderDAOImpl;
import lk.ijse.dep7.pos.dao.impl.OrderDetailDAOImpl;
import lk.ijse.dep7.pos.dao.impl.QueryDAOImpl;
import lk.ijse.dep7.pos.dto.ItemDTO;
import lk.ijse.dep7.pos.dto.OrderDTO;
import lk.ijse.dep7.pos.dto.OrderDetailDTO;
import lk.ijse.dep7.pos.entity.Customer;
import lk.ijse.dep7.pos.entity.Order;
import lk.ijse.dep7.pos.entity.OrderDetail;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static lk.ijse.dep7.pos.service.util.EntityDTOMapper.*;

public class OrderService {

    private final Connection connection;
    private final CustomerDAOImpl customerDAOImpl;
    private final OrderDAOImpl orderDAOImpl;
    private final OrderDetailDAOImpl orderDetailDAOImpl;
    private final QueryDAOImpl queryDAOImpl;

    public OrderService(Connection connection) {
        this.connection = connection;
        this.orderDAOImpl = new OrderDAOImpl(connection);
        this.orderDetailDAOImpl = new OrderDetailDAOImpl(connection);
        this.queryDAOImpl = new QueryDAOImpl(connection);
        this.customerDAOImpl = new CustomerDAOImpl(connection);
    }

    public void saveOrder(OrderDTO order) throws Exception {

        final CustomerService customerService = new CustomerService(connection);
        final ItemService itemService = new ItemService(connection);
        final String orderId = order.getOrderId();
        final String customerId = order.getCustomerId();

        try {
            connection.setAutoCommit(false);

            if (orderDAOImpl.existsOrderById(orderId)) {
                throw new RuntimeException("Invalid Order ID." + orderId + " already exists");
            }

            if (!customerService.existCustomer(customerId)) {
                throw new RuntimeException("Invalid Customer ID." + customerId + " doesn't exist");
            }

            orderDAOImpl.saveOrder(fromOrderDTO(order));

            for (OrderDetailDTO detail : order.getOrderDetails()) {
                orderDetailDAOImpl.saveOrderDetail(fromOrderDetailDTO(orderId, detail));

                ItemDTO item = itemService.findItem(detail.getItemCode());
                item.setQtyOnHand(item.getQtyOnHand() - detail.getQty());
                itemService.updateItem(item);
            }

            connection.commit();

        } catch (SQLException e) {
            failedOperationExecutionContext(connection::rollback);
        } catch (Throwable t) {
            failedOperationExecutionContext(connection::rollback);
            throw t;
        } finally {
            failedOperationExecutionContext(() -> connection.setAutoCommit(true));
        }

    }

    public long getSearchOrdersCount(String query) throws Exception {
        return queryDAOImpl.countOrders(query);
    }

    public List<OrderDTO> searchOrders(String query, int page, int size) throws Exception {
        // CustomEntity => OrderDTO
        // List<CustomEntity> => List<OrderDTO>
        return toOrderDTO2(queryDAOImpl.findOrders(query, page, size));
    }

    public OrderDTO searchOrder(String orderId) throws Exception {
        Order order = orderDAOImpl.findOrderById(orderId).orElseThrow(() -> {
            throw new RuntimeException("Invalid Order ID: " + orderId);
        });
        Customer customer = customerDAOImpl.findCustomerById(order.getCustomerId()).get();
        BigDecimal orderTotal = orderDetailDAOImpl.findOrderTotal(orderId).get();
        List<OrderDetail> orderDetails = orderDetailDAOImpl.findOrderDetailsByOrderId(orderId);

        return toOrderDTO(order, customer, orderTotal, orderDetails);
    }

    public String generateNewOrderId() throws Exception {
        String id = orderDAOImpl.getLastOrderId();
        if (id != null) {
            return String.format("OD%03d", (Integer.parseInt(id.replace("OD", "")) + 1));
        } else {
            return "OD001";
        }

    }

    private void failedOperationExecutionContext(ExecutionContext context) {
        try {
            context.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save the order", e);
        }
    }

    @FunctionalInterface
    interface ExecutionContext {
        void execute() throws SQLException;
    }

}
