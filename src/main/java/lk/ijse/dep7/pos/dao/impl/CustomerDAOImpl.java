package lk.ijse.dep7.pos.dao.impl;

import lk.ijse.dep7.pos.dao.CustomerDAO;
import lk.ijse.dep7.pos.entity.Customer;
import lk.ijse.dep7.pos.entity.SuperEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {

    private final Connection connection;

    public CustomerDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(SuperEntity entity) throws Exception {
        PreparedStatement stm = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?)");
        Customer customer = (Customer) entity;
        stm.setString(1, customer.getId());
        stm.setString(2, customer.getName());
        stm.setString(3, customer.getAddress());
        stm.executeUpdate();
    }

    @Override
    public void update(SuperEntity entity) throws Exception {
        PreparedStatement stm = connection.prepareStatement("UPDATE customer SET name=?, address=? WHERE id=?");
        Customer customer = (Customer) entity;
        stm.setString(1, customer.getName());
        stm.setString(2, customer.getAddress());
        stm.setString(3, customer.getId());
        stm.executeUpdate();
    }

    @Override
    public void deleteById(Object key) throws Exception {
        PreparedStatement stm = connection.prepareStatement("DELETE FROM customer WHERE id=?");
        stm.setString(1, key.toString());
        stm.executeUpdate();
    }

    @Override
    public Optional<SuperEntity> findById(Object key) throws Exception {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM customer WHERE id=?");
        stm.setString(1, key.toString());
        ResultSet rst = stm.executeQuery();
        return (rst.next()) ? Optional.of(new Customer(key.toString(), rst.getString("name"), rst.getString("address"))) : Optional.empty();
    }

    @Override
    public List<SuperEntity> findAll() throws Exception {
        List<SuperEntity> customersList = new ArrayList<>();

        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM customer");

        while (rst.next()) {
            customersList.add(new Customer(rst.getString("id"), rst.getString("name"), rst.getString("address")));
        }

        return customersList;
    }

    @Override
    public long count() throws Exception {
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT COUNT(*) FROM customer");
        rst.next();
        return rst.getLong(1);
    }

    @Override
    public boolean existsById(Object key) throws Exception {
        PreparedStatement stm = connection.prepareStatement("SELECT id FROM customer WHERE id=?");
        stm.setString(1, key.toString());
        return stm.executeQuery().next();
    }

    @Override
    public List<SuperEntity> findAll(int page, int size) throws Exception {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM customer LIMIT ? OFFSET ?;");
        stm.setObject(1, size);
        stm.setObject(2, size * (page - 1));
        ResultSet rst = stm.executeQuery();
        List<SuperEntity> customersList = new ArrayList<>();

        while (rst.next()) {
            customersList.add(new Customer(rst.getString("id"), rst.getString("name"), rst.getString("address")));
        }
        return customersList;
    }

    @Override
    public String getLastCustomerId() throws Exception {
        ResultSet rst = connection.createStatement().executeQuery("SELECT id FROM customer ORDER BY id DESC LIMIT 1;");
        return rst.next() ? rst.getString("id") : null;
    }

}
