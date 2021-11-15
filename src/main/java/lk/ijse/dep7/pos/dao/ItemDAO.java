package lk.ijse.dep7.pos.dao;

import lk.ijse.dep7.pos.entity.Item;

public interface ItemDAO extends CrudDAO<Item, String> {

    String getLastItemCode() throws Exception;

}
