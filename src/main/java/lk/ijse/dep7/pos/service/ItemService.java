package lk.ijse.dep7.pos.service;

import lk.ijse.dep7.pos.dao.DAOFactory;
import lk.ijse.dep7.pos.dao.DAOType;
import lk.ijse.dep7.pos.dao.custom.ItemDAO;
import lk.ijse.dep7.pos.dto.ItemDTO;

import java.util.List;

import static lk.ijse.dep7.pos.service.util.EntityDTOMapper.*;

public class ItemService {

    private final ItemDAO itemDAO;

    public ItemService() {
        itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
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
        return toItemDTO(itemDAO.findById(code).orElseThrow(() -> new RuntimeException("There is no such item associated with the id " + code)));
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
