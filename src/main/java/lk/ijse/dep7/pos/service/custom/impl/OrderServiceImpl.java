package lk.ijse.dep7.pos.service.custom.impl;

import lk.ijse.dep7.pos.dao.DAOFactory;
import lk.ijse.dep7.pos.dao.DAOType;
import lk.ijse.dep7.pos.dao.HibernateUtil;
import lk.ijse.dep7.pos.dao.custom.*;
import lk.ijse.dep7.pos.dto.OrderDTO;
import lk.ijse.dep7.pos.dto.OrderDetailDTO;
import lk.ijse.dep7.pos.entity.Customer;
import lk.ijse.dep7.pos.entity.Item;
import lk.ijse.dep7.pos.entity.Order;
import lk.ijse.dep7.pos.entity.OrderDetail;
import lk.ijse.dep7.pos.service.ServiceFactory;
import lk.ijse.dep7.pos.service.ServiceType;
import lk.ijse.dep7.pos.service.custom.CustomerService;
import lk.ijse.dep7.pos.service.custom.OrderService;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.List;

import static lk.ijse.dep7.pos.service.util.EntityDTOMapper.*;


public class OrderServiceImpl implements OrderService {
    private final CustomerDAO customerDAO;
    private final OrderDAO orderDAO;
    private final OrderDetailDAO orderDetailDAO;
    private final QueryDAO queryDAO;
    private final ItemDAO itemDAO;

    public OrderServiceImpl() {
        this.orderDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER);
        this.orderDetailDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER_DETAIL);
        this.queryDAO = DAOFactory.getInstance().getDAO(DAOType.QUERY);
        this.customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);
        this.itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
    }

    @Override
    public void saveOrder(OrderDTO order) throws Exception {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            orderDAO.setSession(session);
            orderDetailDAO.setSession(session);
            customerDAO.setSession(session);
            itemDAO.setSession(session);

            final CustomerService customerService = ServiceFactory.getInstance().getService(ServiceType.CUSTOMER);
            final String orderId = order.getOrderId();
            final String customerId = order.getCustomerId();

            session.beginTransaction();

            if (orderDAO.existsById(orderId)) {
                throw new RuntimeException("Invalid Order ID." + orderId + " already exists");
            }

            if (!customerService.existCustomer(customerId)) {
                throw new RuntimeException("Invalid Customer ID." + customerId + " doesn't exist");
            }

            orderDAO.save(fromOrderDTO(order));

            for (OrderDetailDTO detail : order.getOrderDetails()) {
                /* We don't need to save order details explicitly here because we have set bi directional relationship between order and order details */
                /* When we save order it automatically saves order details too */
                //orderDetailDAO.save(fromOrderDetailDTO(orderId, detail));

                Item item = itemDAO.findById(detail.getItemCode()).get();
                item.setQtyOnHand(item.getQtyOnHand() - detail.getQty());
                //itemService.updateItem(item);
            }

            session.getTransaction().commit();
        }

    }

    @Override
    public long getSearchOrdersCount(String query) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            queryDAO.setSession(session);

            return queryDAO.countOrders(query);
        }
    }

    @Override
    public List<OrderDTO> searchOrders(String query, int page, int size) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            queryDAO.setSession(session);

            return toOrderDTO2(queryDAO.findOrders(query, page, size));
        }
    }

    @Override
    public OrderDTO searchOrder(String orderId) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            queryDAO.setSession(session);
            customerDAO.setSession(session);
            orderDetailDAO.setSession(session);

            Order order = orderDAO.findById(orderId).orElseThrow(() -> new RuntimeException("Invalid Order ID: " + orderId));
            Customer customer = customerDAO.findById(order.getCustomer().getId()).get();
            BigDecimal orderTotal = orderDetailDAO.findOrderTotal(orderId).get();
            List<OrderDetail> orderDetails = orderDetailDAO.findOrderDetailsByOrderId(orderId);

            return toOrderDTO(order, customer, orderTotal, orderDetails);
        }
    }

    @Override
    public String generateNewOrderId() throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            orderDAO.setSession(session);

            String id = orderDAO.getLastOrderId();
            if (id != null) {
                return String.format("OD%03d", (Integer.parseInt(id.replace("OD", "")) + 1));
            } else {
                return "OD001";
            }
        }
    }

}
