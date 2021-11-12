package lk.ijse.dep7.pos.dao;

import lk.ijse.dep7.pos.entity.OrderDetail;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderDetailDAO extends CrudDAO {

    Optional<BigDecimal> findOrderTotal(String orderId) throws Exception;

    List<OrderDetail> findOrderDetailsByOrderId(String orderId) throws Exception;
}
