package lk.ijse.dep7.pos.service;

public class ServiceFactory {

    private static ServiceFactory serviceFactory;

    private ServiceFactory(){

    }

    public static ServiceFactory getInstance() {
        return (serviceFactory == null)? (serviceFactory = new ServiceFactory()): serviceFactory;
    }

}
