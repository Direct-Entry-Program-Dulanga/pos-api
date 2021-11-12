package lk.ijse.dep7.pos.dao;

public interface CustomerDAO extends CrudDAO {

    String getLastCustomerId() throws Exception;
}
