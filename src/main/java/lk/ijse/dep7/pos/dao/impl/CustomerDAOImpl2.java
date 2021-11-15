package lk.ijse.dep7.pos.dao.impl;

import lk.ijse.dep7.pos.dao.CustomerDAO;
import lk.ijse.dep7.pos.entity.Customer;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl2 implements CustomerDAO {

    public CustomerDAOImpl2(Connection connection) {
    }

    @Override
    public void save(Customer entity) throws Exception {

    }

    @Override
    public void update(Customer entity) throws Exception {

    }

    @Override
    public void deleteById(String key) throws Exception {

    }

    @Override
    public Optional<Customer> findById(String key) throws Exception {
        return Optional.empty();
    }

    @Override
    public List<Customer> findAll() throws Exception {
        return null;
    }

    @Override
    public long count() throws Exception {
        return 0;
    }

    @Override
    public boolean existsById(String key) throws Exception {
        return false;
    }

    @Override
    public List<Customer> findAll(int page, int size) throws Exception {
        return null;
    }

    @Override
    public String getLastCustomerId() throws Exception {
        return null;
    }
}
