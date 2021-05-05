package sample;

import sample.dao.DishDAO;
import sample.dao.UserDAO;
import sample.dto.Admin;
import sample.dto.Customer;
import sample.dto.Dish;

import java.sql.*;
import java.util.logging.Logger;

public class JDBCClient {
    private static final String HOST = "jdbc:mysql://localhost:3306/";
    private static final String DB = "test";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public JDBCClient() {
    }

    public Connection connection = connect();

    public Connection connect() {
        Connection conn;
        String url = HOST + DB;

        try {
            Class.forName(DRIVER);
            logger.info("DRIVER " + DRIVER + " [OK]");
        } catch (ClassNotFoundException e) {
            logger.severe("DRIVER FAILED");
            e.printStackTrace();
            return null;
        }

        try {
            logger.info("CONNECTING TO DATABASE");
            conn = DriverManager.getConnection(url, USERNAME, PASSWORD);
            logger.info("CONNECTION SUCCESSFUL");
        } catch (SQLException e) {
            logger.severe("CONNECTION FAILED");
            e.printStackTrace();
            return null;
        }

        return conn;
    }

    /**
     * Creates tables for DTOs, basic dishes and dummy users
     */
    public void initializeDatabase() {
        createTablesIfNotExist();
    }

    public void createTablesIfNotExist() {
        try {
            DatabaseMetaData md = connection.getMetaData();
            logger.info("CREATING TABLES");

            if (!md.getTables(null, null, "Dish", null).first()) {
                logger.info("TABLE CREATED: DISH");
                connection.createStatement().execute(
                        "CREATE TABLE Dish(" +
                                "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                                "name VARCHAR(50)," +
                                "price DECIMAL(4,2));"
                );
                createDummyDishes();
            }

            if (!md.getTables(null, null, "Address", null).first()) {
                logger.info("TABLE CREATED: ADDRESS");
                connection.createStatement().execute(
                        "CREATE TABLE Address(" +
                                "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                                "street VARCHAR(50)," +
                                "housenumber VARCHAR(20)," +
                                "zipcode INTEGER);"
                );
            }

            if (!md.getTables(null, null, "User", null).first()) {
                logger.info("TABLE CREATED: USER");
                connection.createStatement().execute(
                        "CREATE TABLE User(" +
                                "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                                "usertype VARCHAR(8) NOT NULL," +
                                "firstname VARCHAR(50)," +
                                "lastname VARCHAR(50)," +
                                "gender CHAR(1)," +
                                "email VARCHAR(50) NOT NULL," +
                                "password VARCHAR(50) NOT NULL," +
                                "addressid INTEGER," +
                                "FOREIGN KEY(addressid) REFERENCES Address(id));"
                );
                createDummyUsers();
            }
            if (!md.getTables(null, null, "Dishorder", null).first()) {
                logger.info("TABLE CREATED: ORDER");
                connection.createStatement().execute(
                        "CREATE TABLE Dishorder(" +
                                "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                                "userid INTEGER," +
                                "orderdate DATE," +
                                "FOREIGN KEY(userid) REFERENCES User(id));"
                );
            }

            if (!md.getTables(null, null, "OrderPosition", null).first()) {
                logger.info("TABLE CREATED: ORDERPOSITION");
                connection.createStatement().execute(
                        "CREATE TABLE OrderPosition(" +
                                "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                                "orderid INTEGER," +
                                "dishid INTEGER," +
                                "amount INTEGER," +
                                "FOREIGN KEY(orderid) REFERENCES Dishorder(id)," +
                                "FOREIGN KEY(dishid) REFERENCES Dish(id));"
                );
            }
        } catch (SQLException e) {
            logger.severe("TABLES COULD NOT BE CREATED");
            e.printStackTrace();
        }
    }

    public void createDummyUsers() {
        UserDAO dao = new UserDAO();
        dao.save(new Customer("Debby", "Dummy", new Customer.Address("Dummy Street", "13a", 11111), 'f', "user1@dummy.com", "topsecret"));
        dao.save(new Customer("Max", "Mustermann", new Customer.Address("Dummy Street", "13b", 11111), 'm', "user2@dummy.com", "topsecret"));
        dao.save(new Admin("Hannah", "Tönjes",'f', "admin2@dummy.com", "topsecret"));
        dao.save(new Admin("Marco", "Lenz",'m', "admin1@dummy.com", "topsecret"));
    }

    public void createDummyDishes() {
        logger.info("CREATING DUMMY DISHES");
        DishDAO dishdao = new DishDAO();
        Dish[] dishes = {
                new Dish("Pizza Hawaii", 6.30),
                new Dish("Pizza Tuna", 7.40),
                new Dish("Pizza Margherita", 4.0)};

        for (Dish d: dishes)
            dishdao.save(d);
    }

    public void disconnect() {
        try {
            connection.close();
            logger.info("CONNECTION CLOSED[OK]");
        } catch (SQLException e) {
            logger.severe("CONNECTION COULD NOT BE CLOSED");
            e.printStackTrace();
        }
    }
}
