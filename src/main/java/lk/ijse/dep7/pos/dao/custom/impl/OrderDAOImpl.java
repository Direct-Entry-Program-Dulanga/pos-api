package lk.ijse.dep7.pos.dao.custom.impl;

import lk.ijse.dep7.pos.dao.CrudDAOImpl;
import lk.ijse.dep7.pos.dao.custom.OrderDAO;
import lk.ijse.dep7.pos.entity.Order;

public class OrderDAOImpl extends CrudDAOImpl<Order, String> implements OrderDAO {

    @Override
    public String getLastOrderId() {
        return (String) session.createNativeQuery("SELECT id FROM `order` ORDER BY id DESC LIMIT 1").uniqueResult();
    }
}
