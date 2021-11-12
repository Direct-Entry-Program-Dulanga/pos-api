package lk.ijse.dep7.pos.dao;

import lk.ijse.dep7.pos.entity.SuperEntity;

import java.util.List;
import java.util.Optional;

public interface CrudDAO {

    void save(SuperEntity entity) throws Exception;

    void update(SuperEntity entity) throws Exception;

    void deleteById(Object key) throws Exception;

    Optional<SuperEntity> findById(Object key) throws Exception;

    List<SuperEntity> findAll() throws Exception;

    long count() throws Exception;

    boolean existsById(Object key) throws Exception;

    List<SuperEntity> findAll(int page, int size) throws Exception;

}
