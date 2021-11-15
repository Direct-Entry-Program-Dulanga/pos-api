package lk.ijse.dep7.pos.dao;

import lk.ijse.dep7.pos.entity.Order;

public interface OrderDAO extends CrudDAO<Order, String> {

    String getLastOrderId() throws Exception;
}
