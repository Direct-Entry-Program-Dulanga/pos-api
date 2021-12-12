package lk.ijse.dep7.pos.dao.custom.impl;

import lk.ijse.dep7.pos.dao.CrudDAOImpl;
import lk.ijse.dep7.pos.entity.OrderDetail;
import lk.ijse.dep7.pos.entity.OrderDetailPK;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class OrderDetailDAOImpl extends CrudDAOImpl<OrderDetail, OrderDetailPK> {

    @Override
    public Optional<BigDecimal> findOrderTotal(String orderId) {
        BigDecimal total = (BigDecimal) session.
                createNativeQuery("SELECT SUM(unit_price * qty) as total FROM order_detail WHERE order_id=?1 GROUP BY order_id;")
                .setParameter(1, orderId).uniqueResult();
        return (total == null) ? Optional.empty() : Optional.of(total);
    }

    @Override
    public List<OrderDetail> findOrderDetailsByOrderId(String orderId) {
        return session.createNativeQuery("SELECT * FROM `order_detail` WHERE order_id =?1")
                .setParameter(1, orderId).addEntity(OrderDetail.class).list();
    }

}
