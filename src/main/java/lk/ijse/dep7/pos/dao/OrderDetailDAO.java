package lk.ijse.dep7.pos.dao;

import lk.ijse.dep7.pos.entity.OrderDetail;
import lk.ijse.dep7.pos.entity.OrderDetailPK;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderDetailDAO {

    void saveOrderDetail(OrderDetail orderDetail) throws Exception;

    void updateOrderDetail(OrderDetail orderDetail) throws Exception;

    void deleteOrderDetailByPK(OrderDetailPK orderDetailPK) throws Exception;

    Optional<OrderDetail> findOrderDetailByPK(OrderDetailPK orderDetailPK) throws Exception;

    List<OrderDetail> findAllOrderDetails() throws Exception;

    long countOrderDetails() throws Exception;

    boolean existsOrderDetailByPK(OrderDetailPK orderDetailPK) throws Exception;

    Optional<BigDecimal> findOrderTotal(String orderId) throws Exception;

    List<OrderDetail> findOrderDetailsByOrderId(String orderId) throws Exception;
}
