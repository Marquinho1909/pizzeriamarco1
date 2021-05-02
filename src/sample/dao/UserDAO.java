package sample.dao;

import sample.JDBCClient;
import sample.dto.Admin;
import sample.dto.Customer;
import sample.dto.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO implements DAO<User> {
    public static User loggedInUser = null;

    @Override
    public Optional<User> get(int id) {
        try (Connection connection = new JDBCClient().connection) {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM USER u LEFT JOIN Address a ON u.addressid = a.id WHERE u.id=" + id + ";");
            if (result.first())
                return Optional.of(convertToCostumerOrAdmin(result));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = new JDBCClient().connection) {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM USER u LEFT JOIN Address a ON u.addressid = a.id;");

            while (result.next())
                users.add(convertToCostumerOrAdmin(result));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<User> getAllCustomers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = new JDBCClient().connection) {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM USER u LEFT JOIN Address a ON u.addressid = a.id WHERE u.usertype='Costumer';");

            while (result.next())
                users.add(convertToCostumer(result));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<User> getAllAdmin() {
        List<User> users = new ArrayList<>();
        try (Connection connection = new JDBCClient().connection) {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM USER WHERE usertype='Admin';");

            while (result.next())
                users.add(convertToAdmin(result));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void save(User user) {
        try (Connection connection = new JDBCClient().connection) {
            int aId = 0;
            if (user.getClass() == Customer.class) {
                Customer customer = (Customer) user;
                PreparedStatement prepA = connection.prepareStatement("INSERT INTO Address (street, housenumber, zipcode) VALUES (?,?,?);", Statement.RETURN_GENERATED_KEYS);
                prepA.setString(1, customer.getAddress().getStreet());
                prepA.setString(2, customer.getAddress().getHouseNumber());
                prepA.setInt(3, customer.getAddress().getZipCode());
                prepA.execute();

                ResultSet rs = prepA.getGeneratedKeys();

                if (rs.first())
                    aId = rs.getInt(1);
                else
                    throw new SQLException("GENERATED KEY NOT FOUND");
            }

            PreparedStatement prep = aId != 0 ?connection.prepareStatement(
                    "INSERT INTO User(firstname, lastname, gender, email, password, usertype, addressid) " +
                            "VALUES(?,?,?,?,?,?,?);") :
                    connection.prepareStatement(
                    "INSERT INTO User(firstname, lastname, gender, email, password, usertype) " +
                            "VALUES(?,?,?,?,?,?);");

            prep.setString(1, user.getFirstName());
            prep.setString(2, user.getLastname());
            prep.setString(3, "" + user.getGender());
            prep.setString(4, user.getEmail());
            prep.setString(5, user.getPassword());
            prep.setString(6, user.getClass() == Admin.class ? "Admin" : "User");
            if (aId != 0)
                prep.setInt(7, aId);

            prep.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int id, User user) {
        try (Connection connection = new JDBCClient().connection) {
            ResultSet rs = connection.createStatement().executeQuery("SELECT addressid FROM User WHERE id=" + id + ";");
            if (user.getClass() == Customer.class) {
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
            prep.setInt(6, user.getId());

            prep.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = new JDBCClient().connection) {
            connection.createStatement().executeUpdate("DELETE FROM User WHERE id=" + id + ";");
        } catch (SQLException e) {
            e.printStackTrace();
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
}
