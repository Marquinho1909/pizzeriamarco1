package sample.dao;

import sample.JDBCClient;
import sample.dto.Admin;
import sample.dto.Coupon;
import sample.dto.Customer;
import sample.dto.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {
    CouponDAO couponDAO = new CouponDAO();
    Connection connection;

    public UserDAO() {
        connection = new JDBCClient().connection;
    }

    public Optional<User> get(int id) {
        try {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM USER u LEFT JOIN Address a ON u.addressid = a.id WHERE u.id=" + id + ";");
            if (result.first())
                return Optional.of(convertToCostumerOrAdmin(result));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM USER u LEFT JOIN Address a ON u.addressid = a.id;");
            while (result.next())
                users.add(convertToCostumerOrAdmin(result));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> users = new ArrayList<>();
        try {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM USER u LEFT JOIN Address a ON u.addressid = a.id WHERE u.usertype='Customer';");
            while (result.next())
                users.add(convertToCostumer(result));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<User> getAllAdmin() {
        List<User> users = new ArrayList<>();
        try {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM USER WHERE usertype='Admin';");
            while (result.next())
                users.add(convertToAdmin(result));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public Coupon getCustomerCoupon(Customer customer) {
        try {
            PreparedStatement prepA = connection.prepareStatement("SELECT id FROM Address WHERE street=? AND housenumber=? AND zipcode=?;");
            prepA.setString(1, customer.getAddress().getStreet());
            prepA.setString(2, customer.getAddress().getHouseNumber());
            prepA.setInt(3, customer.getAddress().getZipCode());
            ResultSet rs = prepA.executeQuery();
            if (rs.first())
                return couponDAO.getCouponByAddressId(rs.getInt(1));
            else throw new SQLException();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int save(User user) {
        try {
            int aId = 0;
            if (user.getClass() == Customer.class) {
                Customer customer = (Customer) user;
                // Searches for address in case it already exists
                PreparedStatement prepA = connection.prepareStatement("SELECT id FROM Address WHERE street=? AND housenumber=? AND zipcode=?;");
                prepA.setString(1, customer.getAddress().getStreet());
                prepA.setString(2, customer.getAddress().getHouseNumber());
                prepA.setInt(3, customer.getAddress().getZipCode());
                ResultSet rs = prepA.executeQuery();
                if (rs.first()) {
                    // When address already exists, no coupon created
                    aId = rs.getInt(1);
                } else {
                    // When address is new, address and coupon are created
                    prepA = connection.prepareStatement("INSERT INTO Address (street, housenumber, zipcode) VALUES (?,?,?);", Statement.RETURN_GENERATED_KEYS);
                    prepA.setString(1, customer.getAddress().getStreet());
                    prepA.setString(2, customer.getAddress().getHouseNumber());
                    prepA.setInt(3, customer.getAddress().getZipCode());
                    prepA.execute();
                    rs = prepA.getGeneratedKeys();

                    if (rs.first()) {
                        aId = rs.getInt(1);
                        couponDAO.saveByAddressId(new Coupon(0.1), aId);
                    } else
                        throw new SQLException("GENERATED KEY NOT FOUND");
                }
            }

            PreparedStatement prep = aId != 0 ?connection.prepareStatement(
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
            if (aId != 0)
                prep.setInt(7, aId);

            prep.execute();
            ResultSet rs = prep.getGeneratedKeys();
            if (rs.next())
                return rs.getInt(1);
            else
                throw new SQLException();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean update(int id, User user) {
        try {
            if (user.getClass() == Customer.class) {
                ResultSet rs = connection.createStatement().executeQuery("SELECT addressid FROM User WHERE id=" + id + ";");
                int aId;
                Customer customer = (Customer) user;
                if (rs.first())
                    aId = rs.getInt("addressid");
                else
                    throw new SQLException();

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
            System.out.println("UPDATED USER WITH ID " + id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try {
            connection.createStatement().executeUpdate("DELETE FROM User WHERE id=" + id + ";");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User convertToCostumerOrAdmin(ResultSet users) throws SQLException {
        if (users.getString("usertype").equals("Admin"))
            return convertToAdmin(users);
        else
            return convertToCostumer(users);
    }

    public Customer convertToCostumer(ResultSet costumers) throws SQLException {
        Customer.Address address = new Customer.Address(
                costumers.getString("street"),
                costumers.getString("housenumber"),
                costumers.getInt("zipcode"));

        return new Customer(
                costumers.getInt("id"),
                costumers.getString("firstname"),
                costumers.getString("lastname"),
                address,
                costumers.getString("gender").charAt(0),
                costumers.getString("email"),
                costumers.getString("password"));
    }

    public Admin convertToAdmin(ResultSet admins) throws SQLException {
        return new Admin(
                admins.getInt("id"),
                admins.getString("firstname"),
                admins.getString("lastname"),
                admins.getString("gender").charAt(0),
                admins.getString("email"),
                admins.getString("password"));
    }

    public User getUserByEmailAndPassword(String email, String password) {
        User user = null;
        try (Connection connection = new JDBCClient().connection) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean makeCustomerAdmin(int id) {
        try (Connection connection = new JDBCClient().connection) {
            connection.createStatement().executeUpdate("UPDATE User SET usertype='Admin' WHERE id=" + id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
