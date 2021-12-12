package lk.ijse.dep7.pos.dao.custom.impl;

import lk.ijse.dep7.pos.dao.CrudDAOImpl;
import lk.ijse.dep7.pos.entity.Customer;

public class CustomerDAOImpl extends CrudDAOImpl<Customer, String> {

    @Override
    public String getLastCustomerId() {
        session.flush();
        return (String) session.createNativeQuery("SELECT id FROM customer ORDER BY id DESC LIMIT 1").uniqueResult();
    }

}
