package lk.ijse.dep7.pos.dao;

public interface OrderDAO extends CrudDAO {

    String getLastOrderId() throws Exception;
}
