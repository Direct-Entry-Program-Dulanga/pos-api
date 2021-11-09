package lk.ijse.dep7.pos.dao;

import lk.ijse.dep7.pos.dto.CustomerDTO;
import lk.ijse.dep7.pos.exception.FailedOperationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    private Connection connection;

    public CustomerDAO(Connection connection){
        this.connection = connection;
    }

    public void saveCustomer(CustomerDTO customer) {
        try {
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO customer (id,name, address) VALUES (?,?,?)");
            pstm.setString(1, customer.getId());
            pstm.setString(2, customer.getName());
            pstm.setString(3, customer.getAddress());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save the customer");
        }
    }

    public long getCustomersCount() {
        try {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT COUNT(*) FROM customer");
            rst.next();
            return rst.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get customers count");
        }
    }

    public boolean existsCustomer(String customerId) {
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT id FROM customer WHERE id=?");
            pstm.setString(1, customerId);
            return pstm.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException("Failed operation");
        }
    }

    public void updateCustomer(CustomerDTO customer) {
        try {
            PreparedStatement pstm = connection.prepareStatement("UPDATE customer SET name=?, address=? WHERE id=?");
            pstm.setString(1, customer.getName());
            pstm.setString(2, customer.getAddress());
            pstm.setString(3, customer.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update the customer " + customer.getId(), e);
        }
    }

    public void deleteCustomer(String customerId) {
        try {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM customer WHERE id=?");
            pstm.setString(1, customerId);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete the customer " + customerId, e);
        }
    }

    public CustomerDTO findCustomer(String customerId) {
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM customer WHERE id=?");
            pstm.setString(1, customerId);
            ResultSet rst = pstm.executeQuery();
            rst.next();
            return new CustomerDTO(customerId, rst.getString("name"), rst.getString("address"));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find the customer " + customerId, e);
        }
    }

    public List<CustomerDTO> findAllCustomers(){
        try {
            List<CustomerDTO> customersList = new ArrayList<>();

            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM customer");

            while (rst.next()) {
                customersList.add(new CustomerDTO(rst.getString("id"), rst.getString("name"), rst.getString("address")));
            }

            return customersList;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find customers", e);
        }
    }

    public List<CustomerDTO> findAllCustomers(int page, int size){
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM customer LIMIT ? OFFSET ?;");
            stm.setObject(1, size);
            stm.setObject(2, size * (page - 1));
            ResultSet rst = stm.executeQuery();
            List<CustomerDTO> customersList = new ArrayList<>();

            while (rst.next()) {
                customersList.add(new CustomerDTO(rst.getString("id"), rst.getString("name"), rst.getString("address")));
            }
            return customersList;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch customers", e);
        }
    }

    public String getLastCustomerId(){
        try {
            ResultSet rst = connection.createStatement().executeQuery("SELECT id FROM customer ORDER BY id DESC LIMIT 1;");

            if (rst.next()) {
                String id = rst.getString("id");
                return id;
            } else {
                return null;
            }
        }catch (Exception e){
            throw new RuntimeException("Failed operation");
        }
    }

}
