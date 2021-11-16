package lk.ijse.dep7.pos.service.custom.impl;

import lk.ijse.dep7.pos.dao.DAOFactory;
import lk.ijse.dep7.pos.dao.DAOType;
import lk.ijse.dep7.pos.dao.custom.CustomerDAO;
import lk.ijse.dep7.pos.dto.CustomerDTO;

import java.util.List;

import static lk.ijse.dep7.pos.service.util.EntityDTOMapper.*;

public class CustomerServiceImpl {

    private final CustomerDAO customerDAO;

    public CustomerServiceImpl() {
        customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);
    }

    public void saveCustomer(CustomerDTO customer) throws Exception {
        if (existCustomer(customer.getId())) {
            throw new RuntimeException(customer.getId() + " already exists");
        }
        customerDAO.save(fromCustomerDTO(customer));
    }

    public long getCustomersCount() throws Exception {
        return customerDAO.count();
    }

    boolean existCustomer(String id) throws Exception {
        return customerDAO.existsById(id);
    }

    public void updateCustomer(CustomerDTO customer) throws Exception {
        if (!existCustomer(customer.getId())) {
            throw new RuntimeException("There is no such customer associated with the id " + customer.getId());
        }
        customerDAO.update(fromCustomerDTO(customer));
    }

    public void deleteCustomer(String id) throws Exception {
        if (!existCustomer(id)) {
            throw new RuntimeException("There is no such customer associated with the id " + id);
        }
        customerDAO.deleteById(id);
    }

    public CustomerDTO findCustomer(String id) throws Exception {
        return toCustomerDTO(customerDAO.findById(id).orElseThrow(() -> new RuntimeException("There is no such customer associated with the id " + id)));
    }

    public List<CustomerDTO> findAllCustomers() throws Exception {
        return toCustomerDTOList(customerDAO.findAll());
    }

    public List<CustomerDTO> findAllCustomers(int page, int size) throws Exception {
        return toCustomerDTOList(customerDAO.findAll(page, size));
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
