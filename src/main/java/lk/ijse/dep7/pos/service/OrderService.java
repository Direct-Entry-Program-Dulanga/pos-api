package lk.ijse.dep7.pos.service;

import lk.ijse.dep7.pos.dao.CustomerDAO;
import lk.ijse.dep7.pos.dao.OrderDAO;
import lk.ijse.dep7.pos.dao.OrderDetailDAO;
import lk.ijse.dep7.pos.dao.QueryDAO;
import lk.ijse.dep7.pos.dto.ItemDTO;
import lk.ijse.dep7.pos.dto.OrderDTO;
import lk.ijse.dep7.pos.dto.OrderDetailDTO;
import lk.ijse.dep7.pos.entity.Customer;
import lk.ijse.dep7.pos.entity.Order;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static lk.ijse.dep7.pos.service.util.EntityDTOMapper.*;

public class OrderService {

    private final Connection connection;
    private final CustomerDAO customerDAO;
    private final OrderDAO orderDAO;
    private final OrderDetailDAO orderDetailDAO;
    private final QueryDAO queryDAO;

    public OrderService(Connection connection) {
        this.connection = connection;
        this.orderDAO = new OrderDAO(connection);
        this.orderDetailDAO = new OrderDetailDAO(connection);
        this.queryDAO = new QueryDAO(connection);
        this.customerDAO = new CustomerDAO(connection);
    }

    public void saveOrder(OrderDTO order) throws Exception {

        final CustomerService customerService = new CustomerService(connection);
        final ItemService itemService = new ItemService(connection);
        final String orderId = order.getOrderId();
        final String customerId = order.getCustomerId();

        try {
            connection.setAutoCommit(false);

            if (orderDAO.existsOrderById(orderId)) {
                throw new RuntimeException("Invalid Order ID." + orderId + " already exists");
            }

            if (!customerService.existCustomer(customerId)) {
                throw new RuntimeException("Invalid Customer ID." + customerId + " doesn't exist");
            }

            orderDAO.saveOrder(fromOrderDTO(order));

            for (OrderDetailDTO detail : order.getOrderDetails()) {
                orderDetailDAO.saveOrderDetail(fromOrderDetailDTO(orderId, detail));

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
        return queryDAO.countOrders(query);
    }

    public List<OrderDTO> searchOrders(String query, int page, int size) throws Exception {
        // CustomEntity => OrderDTO
        // List<CustomEntity> => List<OrderDTO>
        return toOrderDTO2(queryDAO.findOrders(query, page, size));
    }

    public OrderDTO searchOrder(String orderId) throws Exception {
        Order order = orderDAO.findOrderById(orderId).get();
        Customer customer = customerDAO.findCustomerById(order.getCustomerId()).get();

        new OrderDTO(order.getId(),
                order.getDate().toLocalDate(),
                order.getCustomerId(),
                customer.getName()
                );
    }

    public List<OrderDetailDTO> findOrderDetails(String orderId) throws Exception {
        return null;
        //return orderDetailDAO.find(orderId);
    }

    public String generateNewOrderId() throws Exception {
        String id = orderDAO.getLastOrderId();
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
