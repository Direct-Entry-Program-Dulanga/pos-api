package lk.ijse.dep7.pos.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class QueryDAOTest {

    private QueryDAO queryDAO;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep7_pos","root","mysql");
        queryDAO = new QueryDAO(connection);
    }

    @Test
    void searchOrders() throws Exception {
        queryDAO.findOrders("", 1, 5).forEach(System.out::println);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }
}