package lk.ijse.dep7.pos.service;

import lk.ijse.dep7.pos.dao.CustomerDAO;
import lk.ijse.dep7.pos.dto.CustomerDTO;

import java.sql.Connection;
import java.util.List;

import static lk.ijse.dep7.pos.service.util.EntityDTOMapper.*;

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
        customerDAO.saveCustomer(fromCustomerDTO(customer));
    }

    public long getCustomersCount() throws Exception {
        return customerDAO.countCustomers();
    }

    boolean existCustomer(String id) throws Exception {
        return customerDAO.existsCustomerById(id);
    }

    public void updateCustomer(CustomerDTO customer) throws Exception {
        if (!existCustomer(customer.getId())) {
            throw new RuntimeException("There is no such customer associated with the id " + customer.getId());
        }
        customerDAO.updateCustomer(fromCustomerDTO(customer));
    }

    public void deleteCustomer(String id) throws Exception {
        if (!existCustomer(id)) {
            throw new RuntimeException("There is no such customer associated with the id " + id);
        }
        customerDAO.deleteCustomerById(id);
    }

    public CustomerDTO findCustomer(String id) throws Exception {
        return toCustomerDTO(customerDAO.findCustomerById(id).<RuntimeException>orElseThrow(() -> {
            throw new RuntimeException("There is no such customer associated with the id " + id);
        }));
    }

    public List<CustomerDTO> findAllCustomers() throws Exception {
        return toCustomerDTOList(customerDAO.findAllCustomers());
    }

    public List<CustomerDTO> findAllCustomers(int page, int size) throws Exception {
        return toCustomerDTOList(customerDAO.findAllCustomers(page, size));
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
