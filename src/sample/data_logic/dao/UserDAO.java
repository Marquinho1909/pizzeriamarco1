package sample.data_logic.dao;

import sample.JDBCClient;
import sample.data_logic.dto.Admin;
import sample.data_logic.dto.Coupon;
import sample.data_logic.dto.Customer;
import sample.data_logic.dto.User;
import sample.data_logic.idao.iUserDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO for User (Customer and Admin)
 */
public class UserDAO extends DAO implements iUserDAO {
    CouponDAO couponDAO;
    Connection connection;

    public UserDAO(CouponDAO couponDAO) {
        this.couponDAO = couponDAO;
        connection = new JDBCClient().connection;
    }

    /**
     * returns user by id
     *
     * @param id of user
     * @return user of id
     * @throws SQLException sql exception
     */
    @Override
    public Optional<User> get(int id) throws SQLException {
        ResultSet result = connection.createStatement().executeQuery("SELECT * FROM USER u LEFT JOIN Address a ON u.addressid = a.id WHERE u.id=" + id + ";");
        if (result.first())
            return Optional.of(convertToCustomerOrAdmin(result));
        return Optional.empty();
    }

    /**
     * returns all customers in database
     *
     * @return all found customers
     * @throws SQLException sql exception
     */
    @Override
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> users = new ArrayList<>();
        ResultSet result = connection.createStatement().executeQuery("SELECT * FROM USER u LEFT JOIN Address a ON u.addressid = a.id WHERE u.usertype='Customer';");
        while (result.next())
            users.add(convertToCostumer(result));

        return users;
    }

    /**
     * returns coupon of customer
     *
     * @param customer to find coupon for
     * @return coupon if customer has one
     * @throws SQLException sql exception
     */
    @Override
    public Optional<Coupon> getCustomerCoupon(Customer customer) throws SQLException {
        int id = getAddressIdOfAddress(customer.getAddress());
        if (id == 0) throw new SQLException("ADDRESS OF CUSTOMER NOT FOUND");
        return couponDAO.getCouponByAddressId(id);
    }

    /**
     * saves user and their address if user is customer, creates coupon if address is new
     *
     * @param user to be saved
     * @return generated id key of user
     * @throws SQLException sql exception
     */
    @Override
    public int save(User user) throws SQLException {
        int aId = 0;
        if (user.getClass() == Customer.class) {
            Customer customer = (Customer) user;
            // Searches for address in case it already exists
            aId = getAddressIdOfAddress(customer.getAddress());
            if (aId == 0) {
                // When address is new, address and coupon are created
                PreparedStatement prepA = connection.prepareStatement("INSERT INTO Address (street, housenumber, zipcode) VALUES (?,?,?);", Statement.RETURN_GENERATED_KEYS);
                prepA.setString(1, customer.getAddress().getStreet());
                prepA.setString(2, customer.getAddress().getHouseNumber());
                prepA.setInt(3, customer.getAddress().getZipCode());
                prepA.execute();
                ResultSet rs = prepA.getGeneratedKeys();

                if (rs.first()) {
                    aId = rs.getInt(1);
                    couponDAO.saveByAddressId(new Coupon(0.1), aId);
                } else throw new SQLException("GENERATED KEY NOT FOUND");
            }
        }

        PreparedStatement prep = aId != 0 ? connection.prepareStatement(
                "INSERT INTO User(firstname, lastname, gender, email, password, usertype, addressid) " +
                        "VALUES(?,?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS) :
                connection.prepareStatement(
                        "INSERT INTO User(firstname, lastname, gender, email, password, usertype) " +
                                "VALUES(?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);

        prep.setString(1, user.getFirstName());
        prep.setString(2, user.getLastname());
        prep.setString(3, "" + user.getGender());
        prep.setString(4, user.getEmail());
        prep.setString(5, user.getPassword());
        prep.setString(6, user.getClass() == Admin.class ? "Admin" : "Customer");

        if (aId != 0) prep.setInt(7, aId);

        prep.execute();
        ResultSet rs = prep.getGeneratedKeys();
        if (rs.next()) return rs.getInt(1);
        else throw new SQLException();
    }

    /**
     * updates user
     *
     * @param id   of user to be updated
     * @param user new user information
     * @throws SQLException sql exception
     */
    @Override
    public void update(int id, User user) throws SQLException {
        if (user.getClass() == Customer.class) {
            ResultSet rs = connection.createStatement().executeQuery("SELECT addressid FROM User WHERE id=" + id + ";");
            int aId;
            Customer customer = (Customer) user;
            if (rs.first()) aId = rs.getInt("addressid");
            else throw new SQLException();

            PreparedStatement prepA = connection.prepareStatement("UPDATE Address SET street=?, housenumber=?, zipcode=? WHERE id=?;");
            prepA.setString(1, customer.getAddress().getStreet());
            prepA.setString(2, customer.getAddress().getHouseNumber());
            prepA.setInt(3, customer.getAddress().getZipCode());
            prepA.setInt(4, aId);
            prepA.executeUpdate();
        }
        PreparedStatement prep = connection.prepareStatement("UPDATE User SET firstname=?, lastname=?, gender=?, email=?, password=? WHERE id=?;");
        prep.setString(1, user.getFirstName());
        prep.setString(2, user.getLastname());
        prep.setString(3, "" + user.getGender());
        prep.setString(4, user.getEmail());
        prep.setString(5, user.getPassword());
        prep.setInt(6, id);

        prep.executeUpdate();
    }

    /**
     * returns either id of address or 0 if no address was found
     *
     * @param address that is being looked for
     * @return address id of parameter
     * @throws SQLException sql exception
     */
    @Override
    public int getAddressIdOfAddress(Customer.Address address) throws SQLException {
        PreparedStatement prepA = connection.prepareStatement("SELECT id FROM Address WHERE street=? AND housenumber=? AND zipcode=?;");
        prepA.setString(1, address.getStreet());
        prepA.setString(2, address.getHouseNumber());
        prepA.setInt(3, address.getZipCode());
        ResultSet rs = prepA.executeQuery();
        if (rs.first())
            return rs.getInt(1);
        else return 0;
    }

    /**
     * tries to find user with login credentials
     * @param email of user
     * @param password of user
     * @return User if found
     * @throws SQLException sql exception
     */
    @Override
    public Optional<User> getUserByEmailAndPassword(String email, String password) throws SQLException {
        User user = null;
        PreparedStatement prep = connection.prepareStatement("SELECT * FROM User u LEFT JOIN Address a ON u.addressid = a.id WHERE email=? AND password=?;");
        prep.setString(1, email);
        prep.setString(2, password);

        ResultSet rs = prep.executeQuery();
        if (rs.first()) {
            if (rs.getString("usertype").equals("Admin"))
                user = convertToAdmin(rs);
            else
                user = convertToCostumer(rs);
        }
        return Optional.of(user);
    }

    /**
     * makes customer to admin
     * @param id of customer
     * @throws SQLException sql exception
     */
    @Override
    public void makeCustomerAdmin(int id) throws SQLException {
        connection.createStatement().executeUpdate("UPDATE User SET usertype='Admin' WHERE id=" + id);
    }

    /**
     * converts resultset from sql execution to either customer or admin, depending on its type
     *
     * @param user to be converted
     * @return converted user, either of type Admin or Customer
     * @throws SQLException sql exception
     */
    private User convertToCustomerOrAdmin(ResultSet user) throws SQLException {
        if (user.getString("usertype").equals("Admin"))
            return convertToAdmin(user);
        else
            return convertToCostumer(user);
    }

    /**
     * converts ResultSet known to be a customer to a Customer
     *
     * @param customer resultSet to be converted
     * @return converted Customer
     * @throws SQLException sql exception
     */
    private Customer convertToCostumer(ResultSet customer) throws SQLException {
        Customer.Address address = new Customer.Address(
                customer.getString("street"),
                customer.getString("housenumber"),
                customer.getInt("zipcode"));

        return new Customer(
                customer.getInt("id"),
                customer.getString("firstname"),
                customer.getString("lastname"),
                address,
                customer.getString("gender").charAt(0),
                customer.getString("email"),
                customer.getString("password"));
    }

    private Admin convertToAdmin(ResultSet admin) throws SQLException {
        return new Admin(
                admin.getInt("id"),
                admin.getString("firstname"),
                admin.getString("lastname"),
                admin.getString("gender").charAt(0),
                admin.getString("email"),
                admin.getString("password"));
    }
}
