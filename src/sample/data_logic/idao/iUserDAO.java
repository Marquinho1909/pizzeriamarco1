package sample.data_logic.idao;

import sample.data_logic.dto.Coupon;
import sample.data_logic.dto.Customer;
import sample.data_logic.dto.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface iUserDAO {
    Optional<User> get(int id) throws SQLException;
    List<Customer> getAllCustomers() throws SQLException;
    Optional<Coupon> getCustomerCoupon(Customer customer) throws SQLException;
    int save(User user) throws SQLException;
    void update(int id, User user) throws SQLException;
    int getAddressIdOfAddress(Customer.Address address) throws SQLException;
    Optional<User> getUserByEmailAndPassword(String email, String password) throws SQLException;
    void makeCustomerAdmin(int id) throws SQLException;
}
