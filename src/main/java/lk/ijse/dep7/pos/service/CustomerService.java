package lk.ijse.dep7.pos.service;

import lk.ijse.dep7.pos.dao.CustomerDAO;
import lk.ijse.dep7.pos.dto.CustomerDTO;
import lk.ijse.dep7.pos.entity.Customer;
import lk.ijse.dep7.pos.exception.DuplicateIdentifierException;
import lk.ijse.dep7.pos.exception.FailedOperationException;
import lk.ijse.dep7.pos.exception.NotFoundException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerService {

    private CustomerDAO customerDAO;

    public CustomerService() {
    }

    public CustomerService(Connection connection) {
        this.customerDAO = new CustomerDAO(connection);
    }

    public void saveCustomer(CustomerDTO customer) throws Exception {
        if (existCustomer(customer.getId())) {
            throw new RuntimeException(customer.getId() + " already exists");
        }
        customerDAO.saveCustomer(new Customer(customer.getId(), customer.getName(), customer.getAddress()));
    }

    public long getCustomersCount() throws Exception {
        return customerDAO.countCustomers();
    }

    boolean existCustomer(String id) throws Exception {
        return customerDAO.existsCustomerById(id);
    }

    public void updateCustomer(CustomerDTO customer) throws Exception{
        if (!existCustomer(customer.getId())) {
            throw new RuntimeException("There is no such customer associated with the id " + customer.getId());
        }
        customerDAO.updateCustomer(new Customer(customer.getId(), customer.getName(), customer.getAddress()));
    }

    public void deleteCustomer(String id) throws Exception {
        if (!existCustomer(id)) {
            throw new RuntimeException("There is no such customer associated with the id " + id);
        }
        customerDAO.deleteCustomerById(id);
    }

    public CustomerDTO findCustomer(String id) throws Exception {
        return customerDAO.findCustomerById(id).map(c -> new CustomerDTO(c.getId(), c.getName(), c.getAddress())).orElseThrow(() -> {throw new RuntimeException("There is no such customer associated with the id " + id);});
    }

    public List<CustomerDTO> findAllCustomers() throws Exception {
        return customerDAO.findAllCustomers().stream().map(c-> new CustomerDTO(c.getId(), c.getName(), c.getAddress())).collect(Collectors.toList());
    }

    public List<CustomerDTO> findAllCustomers(int page, int size) throws Exception {
        return customerDAO.findAllCustomers(page,size).stream().map(c-> new CustomerDTO(c.getId(), c.getName(), c.getAddress())).collect(Collectors.toList());
    }

    public String generateNewCustomerId() throws Exception {
        String id = customerDAO.getLastCustomerId();
        if (id != null) {
            int newCustomerId = Integer.parseInt(id.replace("C", "")) + 1;
            return String.format("C%03d", newCustomerId);
        } else {
            return "C001";
        }

    }

}
