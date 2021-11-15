package lk.ijse.dep7.pos.service;

import lk.ijse.dep7.pos.dao.ItemDAO;
import lk.ijse.dep7.pos.dao.impl.ItemDAOImpl;
import lk.ijse.dep7.pos.dto.ItemDTO;

import java.sql.Connection;
import java.util.List;

import static lk.ijse.dep7.pos.service.util.EntityDTOMapper.*;

public class ItemService {

    private ItemDAO itemDAO;

    public ItemService() {
    }

    public ItemService(Connection connection) {
        itemDAO = new ItemDAOImpl(connection);
    }

    public void saveItem(ItemDTO item) throws Exception {
        if (existItem(item.getCode())) {
            throw new RuntimeException(item.getCode() + " already exists");
        }
        itemDAO.save(fromItemDTO(item));
    }

    private boolean existItem(String code) throws Exception {
        return itemDAO.existsById(code);
    }

    public void updateItem(ItemDTO item) throws Exception {
        if (!existItem(item.getCode())) {
            throw new RuntimeException("There is no such item associated with the id " + item.getCode());
        }
        itemDAO.update(fromItemDTO(item));
    }

    public void deleteItem(String code) throws Exception {
        if (!existItem(code)) {
            throw new RuntimeException("There is no such item associated with the id " + code);
        }
        itemDAO.deleteById(code);
    }

    public ItemDTO findItem(String code) throws Exception {
        return toItemDTO(itemDAO.findById(code).<RuntimeException>orElseThrow(() -> {
            throw new RuntimeException("There is no such item associated with the id " + code);
        }));
    }

    public List<ItemDTO> findAllItems() throws Exception {
        return toItemDTOList(itemDAO.findAll());
    }

    public List<ItemDTO> findAllItems(int page, int size) throws Exception {
        return toItemDTOList(itemDAO.findAll(page, size));
    }

    public String generateNewItemCode() throws Exception {
        String code = itemDAO.getLastItemCode();

        if (code != null) {
            int newItemCode = Integer.parseInt(code.replace("I", "")) + 1;
            return String.format("I%03d", newItemCode);
        } else {
            return "I001";
        }
    }

}
