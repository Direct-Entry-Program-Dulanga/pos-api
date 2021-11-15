package lk.ijse.dep7.pos.dao;

import lk.ijse.dep7.pos.dao.custom.impl.*;
import lk.ijse.dep7.pos.dao.impl.*;

import java.sql.Connection;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getInstance(){
        return (daoFactory == null)? (daoFactory = new DAOFactory()): daoFactory;
    }

    public <T extends SuperDAO> T getDAO(Connection connection, DAOType dao){
        switch (dao){
            case CUSTOMER:
                return (T) new CustomerDAOImpl(connection);
            case ITEM:
                return (T) new ItemDAOImpl(connection);
            case ORDER:
                return (T) new OrderDAOImpl(connection);
            case ORDER_DETAIL:
                return (T) new OrderDetailDAOImpl(connection);
            case QUERY:
                return (T) new QueryDAOImpl(connection);
            default:
                throw new RuntimeException("Invalid DAO");
        }
    }

//    public CustomerDAO getCustomerDAO(Connection connection){
//        return new CustomerDAOImpl(connection);
//    }
//
//    public ItemDAO getItemDAO(Connection connection){
//        return new ItemDAOImpl(connection);
//    }
//
//    public OrderDAO getOrderDAO(Connection connection){
//        return new OrderDAOImpl(connection);
//    }
//
//    public OrderDetailDAO getOrderDetailDAO(Connection connection){
//        return new OrderDetailDAOImpl(connection);
//    }
//
//    public QueryDAO getQueryDAO(Connection connection){
//        return new QueryDAOImpl(connection);
//    }
}
