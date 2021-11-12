package lk.ijse.dep7.pos.service;

import lk.ijse.dep7.pos.dao.ItemDAO;
import lk.ijse.dep7.pos.dto.ItemDTO;

import java.sql.Connection;
import java.util.List;

import static lk.ijse.dep7.pos.service.util.EntityDTOMapper.*;

public class ItemService {

    private ItemDAO itemDAO;

    public ItemService() {
    }

    public ItemService(Connection connection) {
        itemDAO = new ItemDAO(connection);
    }

    public void saveItem(ItemDTO item) throws Exception {
        if (existItem(item.getCode())) {
            throw new RuntimeException(item.getCode() + " already exists");
        }
        itemDAO.saveItem(fromItemDTO(item));
    }

    private boolean existItem(String code) throws Exception {
        return itemDAO.existsItemByCode(code);
    }

    public void updateItem(ItemDTO item) throws Exception {
        if (!existItem(item.getCode())) {
            throw new RuntimeException("There is no such item associated with the id " + item.getCode());
        }
        itemDAO.updateItem(fromItemDTO(item));
    }

    public void deleteItem(String code) throws Exception {
        if (!existItem(code)) {
            throw new RuntimeException("There is no such item associated with the id " + code);
        }
        itemDAO.deleteItemByCode(code);
    }

    public ItemDTO findItem(String code) throws Exception {
        return toItemDTO(itemDAO.findItemByCode(code).<RuntimeException>orElseThrow(() -> {
            throw new RuntimeException("There is no such item associated with the id " + code);
        }));
    }

    public List<ItemDTO> findAllItems() throws Exception {
        return toItemDTOList(itemDAO.findAllItems());
    }

    public List<ItemDTO> findAllItems(int page, int size) throws Exception {
        return toItemDTOList(itemDAO.findAllItems(page, size));
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
