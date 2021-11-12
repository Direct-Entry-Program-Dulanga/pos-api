package lk.ijse.dep7.pos.dao;

import lk.ijse.dep7.pos.entity.CustomEntity;

import java.util.HashMap;
import java.util.List;

public interface QueryDAO {

    public List<HashMap<String, Object>> findOrders(String query) throws Exception;

    public long countOrders(String query) throws Exception;

    public List<CustomEntity> findOrders(String query, int page, int size) throws Exception;
}
