package lk.ijse.dep7.pos.dao;

import lk.ijse.dep7.pos.entity.SuperEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface CrudDAO<T extends SuperEntity, ID extends Serializable> extends SuperDAO {

    void save(T entity);

    void update(T entity);

    void deleteById(ID key);

    Optional<T> findById(ID key);

    List<T> findAll();

    long count();

    boolean existsById(ID key);

    List<T> findAll(int page, int size);

}
