package lk.ijse.dep7.pos.dao;

public interface ItemDAO extends CrudDAO {

    String getLastItemCode() throws Exception;

}
