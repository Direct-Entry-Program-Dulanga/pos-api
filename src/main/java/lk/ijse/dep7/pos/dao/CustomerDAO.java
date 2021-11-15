package lk.ijse.dep7.pos.dao;

import lk.ijse.dep7.pos.entity.Customer;

public interface CustomerDAO extends CrudDAO<Customer, String> {

    String getLastCustomerId() throws Exception;
}
