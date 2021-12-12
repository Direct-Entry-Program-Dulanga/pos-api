package lk.ijse.dep7.pos.dao;

import lk.ijse.dep7.pos.entity.SuperEntity;
import org.hibernate.Session;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class CrudDAOImpl<T extends SuperEntity, ID extends Serializable> implements CrudDAO<T, ID> {

    protected Session session;
    private final Class<T> entityClsObj;

    public CrudDAOImpl() {
        entityClsObj = (Class<T>) ((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments()[0];
    }


    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public void save(T entity) {
        session.save(entity);
    }

    @Override
    public void update(T entity) {
        session.update(entity);
    }

    @Override
    public void deleteById(ID key) {
        T entity = session.get(entityClsObj, key);
        session.delete(entity);
    }

    @Override
    public Optional<T> findById(ID key) {
        T entity = session.get(entityClsObj, key);
        if (entity == null) {
            return Optional.empty();
        }else {
            return Optional.of(entity);
        }
    }

    @Override
    public List<T> findAll() {
        return session.createQuery("FROM " + entityClsObj.getName()).list();
    }

    @Override
    public long count() {
        return session.createQuery("SELECT COUNT(e) FROM " + entityClsObj.getName() + " e", Long.class).uniqueResult();
    }

    @Override
    public boolean existsById(ID key) {
        return findById(key).isPresent();
    }

    @Override
    public List<T> findAll(int page, int size) {
        return session.createQuery("FROM " + entityClsObj.getName())
                .setFirstResult(size * (page - 1))
                .setMaxResults(size)
                .list();
    }
}
