package lk.ijse.dep7.pos.service;

import lk.ijse.dep7.pos.dao.impl.ItemDAOImpl;
import lk.ijse.dep7.pos.dto.ItemDTO;

import java.sql.Connection;
import java.util.List;

import static lk.ijse.dep7.pos.service.util.EntityDTOMapper.*;

public class ItemService {

    private ItemDAOImpl itemDAOImpl;

    public ItemService() {
    }

    public ItemService(Connection connection) {
        itemDAOImpl = new ItemDAOImpl(connection);
    }

    public void saveItem(ItemDTO item) throws Exception {
        if (existItem(item.getCode())) {
            throw new RuntimeException(item.getCode() + " already exists");
        }
        itemDAOImpl.saveItem(fromItemDTO(item));
    }

    private boolean existItem(String code) throws Exception {
        return itemDAOImpl.existsItemByCode(code);
    }

    public void updateItem(ItemDTO item) throws Exception {
        if (!existItem(item.getCode())) {
            throw new RuntimeException("There is no such item associated with the id " + item.getCode());
        }
        itemDAOImpl.updateItem(fromItemDTO(item));
    }

    public void deleteItem(String code) throws Exception {
        if (!existItem(code)) {
            throw new RuntimeException("There is no such item associated with the id " + code);
        }
        itemDAOImpl.deleteItemByCode(code);
    }

    public ItemDTO findItem(String code) throws Exception {
        return toItemDTO(itemDAOImpl.findItemByCode(code).<RuntimeException>orElseThrow(() -> {
            throw new RuntimeException("There is no such item associated with the id " + code);
        }));
    }

    public List<ItemDTO> findAllItems() throws Exception {
        return toItemDTOList(itemDAOImpl.findAllItems());
    }

    public List<ItemDTO> findAllItems(int page, int size) throws Exception {
        return toItemDTOList(itemDAOImpl.findAllItems(page, size));
    }

    public String generateNewItemCode() throws Exception {
        String code = itemDAOImpl.getLastItemCode();

        if (code != null) {
            int newItemCode = Integer.parseInt(code.replace("I", "")) + 1;
            return String.format("I%03d", newItemCode);
        } else {
            return "I001";
        }
    }

}
