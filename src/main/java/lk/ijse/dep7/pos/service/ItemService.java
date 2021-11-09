package lk.ijse.dep7.pos.service;

import lk.ijse.dep7.pos.dao.ItemDAO;
import lk.ijse.dep7.pos.dto.ItemDTO;
import lk.ijse.dep7.pos.exception.DuplicateIdentifierException;
import lk.ijse.dep7.pos.exception.FailedOperationException;
import lk.ijse.dep7.pos.exception.NotFoundException;

import java.sql.Connection;
import java.util.List;

public class ItemService {

    private ItemDAO itemDAO;

    public ItemService() {
    }

    public ItemService(Connection connection) {
        itemDAO = new ItemDAO(connection);
    }

    public void saveItem(ItemDTO item) throws DuplicateIdentifierException, FailedOperationException {
        if (existItem(item.getCode())) {
            throw new DuplicateIdentifierException(item.getCode() + " already exists");
        }
        itemDAO.saveItem(item);
    }

    private boolean existItem(String code) {
        return itemDAO.existsItem(code);
    }

    public void updateItem(ItemDTO item) throws FailedOperationException, NotFoundException {
        if (!existItem(item.getCode())) {
            throw new NotFoundException("There is no such item associated with the id " + item.getCode());
        }
        itemDAO.updateItem(item);
    }

    public void deleteItem(String code) throws NotFoundException, FailedOperationException {
        if (!existItem(code)) {
            throw new NotFoundException("There is no such item associated with the id " + code);
        }

        itemDAO.deleteItem(code);
    }

    public ItemDTO findItem(String code) throws NotFoundException, FailedOperationException {
        if (!existItem(code)) {
            throw new NotFoundException("There is no such item associated with the id " + code);
        }
        return itemDAO.findItem(code);
    }

    public List<ItemDTO> findAllItems() throws FailedOperationException {
        return itemDAO.findAllItems();
    }

    public List<ItemDTO> findAllItems(int page, int size) throws FailedOperationException {
        return itemDAO.findAllItems(page, size);
    }

    public String generateNewItemCode() throws FailedOperationException {
        String code = itemDAO.getLastItemCode();

        if (code != null) {
            int newItemCode = Integer.parseInt(code.replace("I", "")) + 1;
            return String.format("I%03d", newItemCode);
        } else {
            return "I001";
        }
    }

}
