package lk.ijse.dep7.pos.dao;

import lk.ijse.dep7.pos.dao.impl.*;

import java.sql.Connection;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getInstance(){
        return (daoFactory == null)? (daoFactory = new DAOFactory()): daoFactory;
    }

    public CustomerDAO getCustomerDAO(Connection connection){
        return new CustomerDAOImpl(connection);
    }

    public ItemDAO getItemDAO(Connection connection){
        return new ItemDAOImpl(connection);
    }

    public OrderDAO getOrderDAO(Connection connection){
        return new OrderDAOImpl(connection);
    }

    public OrderDetailDAO getOrderDetailDAO(Connection connection){
        return new OrderDetailDAOImpl(connection);
    }

    public QueryDAO getQueryDAO(Connection connection){
        return new QueryDAOImpl(connection);
    }
}
