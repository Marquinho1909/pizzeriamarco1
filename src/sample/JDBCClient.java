package sample;

import sample.data_logic.dao.CategoryDAO;
import sample.data_logic.dao.DishDAO;
import sample.data_logic.dao.UserDAO;
import sample.data_logic.DAOFactory;
import sample.data_logic.dto.Admin;
import sample.data_logic.dto.Category;
import sample.data_logic.dto.Customer;
import sample.data_logic.dto.Dish;

import java.sql.*;
import java.util.List;
import java.util.logging.Logger;

public class JDBCClient {
    private static final String HOST = "jdbc:mysql://localhost:3306/";
    private static final String DB = "test";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    public Connection connection;

    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public JDBCClient() { connection = connect();}

    public Connection connect() {
        Connection conn;
        String url = HOST + DB;
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            logger.severe("DRIVER FAILED");
            e.printStackTrace();
            return null;
        }
        try {
            conn = DriverManager.getConnection(url, USERNAME, PASSWORD);
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
            logger.info("CREATING TABLES IF NEEDED");

            if (!md.getTables(null, null, "Category", null).first()) {
                logger.info("TABLE CREATED: CATEGORY");
                connection.createStatement().execute(
                        "CREATE TABLE Category(" +
                                "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                                "name VARCHAR(50));"
                );
                createDummyCategories();
            }

            if (!md.getTables(null, null, "Dish", null).first()) {
                logger.info("TABLE CREATED: DISH");
                connection.createStatement().execute(
                        "CREATE TABLE Dish(" +
                                "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                                "name VARCHAR(50)," +
                                "price DECIMAL(4,2)," +
                                "active BOOLEAN);"
                );
            }

            if (!md.getTables(null, null, "Dish_Category", null).first()) {
                logger.info("TABLE CREATED: DISH_CATEGORY");
                connection.createStatement().execute(
                        "CREATE TABLE Dish_Category(" +
                                "dishid INTEGER NOT NULL," +
                                "categoryid INTEGER NOT NULL," +
                                "PRIMARY KEY(dishid, categoryid)," +
                                "FOREIGN KEY(dishid) REFERENCES Dish(id)," +
                                "FOREIGN KEY(categoryid) REFERENCES Category(id));"
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

            if (!md.getTables(null, null, "Coupon", null).first()) {
                logger.info("TABLE CREATED: COUPON");
                connection.createStatement().execute(
                        "CREATE TABLE Coupon(" +
                                "couponid INTEGER AUTO_INCREMENT PRIMARY KEY," +
                                "value DECIMAL(4,2)," +
                                "addressid INTEGER," +
                                "FOREIGN KEY(addressid) REFERENCES Address(id));"
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
                                "discount DECIMAL(4,2)," +
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

    private void createDummyCategories() throws SQLException {
        CategoryDAO dao = (CategoryDAO) DAOFactory.getInstance().getDAO("Category");
        dao.save(new Category("Pizza"));
        dao.save(new Category("Pasta"));
        dao.save(new Category("Lasagne"));
        dao.save(new Category("Salat"));
        dao.save(new Category("Vegetarisch"));
    }

    public void createDummyUsers() throws SQLException {
        UserDAO dao = (UserDAO) DAOFactory.getInstance().getDAO("User");
        dao.save(new Customer("Debby", "Dummy", new Customer.Address("Dummy Street", "13a", 11111), 'f', "user1@dummy.com", "topsecret"));
        dao.save(new Customer("Max", "Mustermann", new Customer.Address("Dummy Street", "13b", 11111), 'm', "user2@dummy.com", "topsecret"));
        dao.save(new Admin("Hannah", "Tönjes", 'f', "admin2@dummy.com", "topsecret"));
        dao.save(new Admin("Marco", "Lenz", 'm', "admin1@dummy.com", "topsecret"));
    }

    public void createDummyDishes() throws SQLException {
        logger.info("CREATING DUMMY DISHES");
        DishDAO dishdao = (DishDAO) DAOFactory.getInstance().getDAO("Dish");
        Dish[] dishes = {
                new Dish("Pizza Hawaii", List.of(new Category("Pizza")), 6.30 , true),
                new Dish("Pizza Tuna", List.of(new Category("Pizza")),7.40, true),
                new Dish("Pizza Margherita", List.of(new Category("Pizza"), new Category("Vegetarisch")),4.0, true),
                new Dish("Pizza Cipolla", List.of(new Category("Pizza"), new Category("Vegetarisch")),4.0, true),
                new Dish("Pizza Napoli", List.of(new Category("Pizza")),4.0, true),
                new Dish("Pizza Salami", List.of(new Category("Pizza")),4.0, true),
                new Dish("Pizza Funghi", List.of(new Category("Pizza"), new Category("Vegetarisch")),4.0, true),
                new Dish("Spaghetti Napoli", List.of(new Category("Pasta"), new Category("Vegetarisch")),5.0, true),
                new Dish("Spaghetti Carbonara", List.of(new Category("Pasta")),6.0, true),
                new Dish("Spaghetti Rinder-Bolognese", List.of(new Category("Pasta")),6.0, true),
                new Dish("Spaghetti al Tonno", List.of(new Category("Pasta")),7.0, true),
                new Dish("Lasagne á la Marco", List.of(new Category("Lasagne")),20, true),
                new Dish("Lasagne á la Marco", List.of(new Category("Lasagne")),20, true),
                new Dish("Caesar Salad", List.of(new Category("Salat")),7, true),
                new Dish("Insalata Tonno", List.of(new Category("Salat")),7, true),
                new Dish("Gemischter Salat", List.of(new Category("Salat")),3, true),
                new Dish("Insalata Caprese", List.of(new Category("Salat")),6, true)};

        for (Dish d : dishes)
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
