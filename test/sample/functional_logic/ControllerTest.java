package sample.functional_logic;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import sample.data_logic.UserSessionSingleton;
import sample.data_logic.dto.*;
import sample.functional_logic.service.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ControllerTest {
    @Mock
    UserService userService;
    @Mock
    CategoryService categoryService;
    @Mock
    CouponService couponService;
    @Mock
    OrderService orderService;
    @Mock
    DishService dishService;


    Controller controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new Controller(userService, categoryService, couponService, orderService, dishService);
    }

    @Test
    public void testUpdateProfile() throws SQLException {
        Customer customer = new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null);

        controller.updateProfile(1, customer);
        Mockito.verify(userService).update(1, customer);
    }

    @Test
    public void testUpdateUser() throws SQLException {
        Customer customer = new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null);

        controller.updateProfile(1, customer);
        Mockito.verify(userService).update(1, customer);
    }

    @Test
    public void testUpdateDish() throws SQLException {
        Dish dish = new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true);

        controller.updateDish(1, dish);
        Mockito.verify(dishService).update(1, dish);
    }

    @Test
    public void testDeleteCouponOfLoggedInUser() throws SQLException {
        UserSessionSingleton.currentSession().setUser(new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", new Coupon(1, 0.3)));
        List<User> users = List.of(new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null));
        Mockito.when(userService.get()).thenReturn(users);
        controller.deleteCouponOfLoggedInUser();

        Mockito.verify(couponService).delete(1);
    }

    @Test
    public void testDeleteCategories() throws SQLException {
        List<Category> categories = List.of(
                new Category(1, "Pizza"),
                new Category(2, "Nudeln")
        );
        controller.deleteCategories(categories);
        Mockito.verify(categoryService).delete(categories);
    }

    @Test
    public void testDeleteOrderHistory() throws SQLException {
        controller.deleteOrderHistory();
        Mockito.verify(orderService).deleteAll();
    }

    @Test
    public void testDeleteDish() throws SQLException {
        controller.deleteDish(1);
        Mockito.verify(dishService).delete(1);
    }

    @Test
    public void testLoadLoggedInUser() {
        UserSessionSingleton.currentSession().setUser(new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null));
        Mockito.when(controller.getUsers()).thenReturn(List.of(new Customer(1, "Debby", "Müller", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null)));
        controller.loadLoggedInUser();

        assertEquals("Müller", UserSessionSingleton.currentSession().getUser().getLastname());
    }

    @Test
    public void testIsUserCustomer() {
        UserSessionSingleton.currentSession().setUser(new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null));

        assertTrue(controller.isUserCustomer());
    }

    @Test
    public void testIsUserAdmin() {
        UserSessionSingleton.currentSession().setUser(new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null));

        assertFalse(controller.isUserAdmin());
    }

    @Test
    public void testSaveCategory() throws SQLException {
        Category category = new Category(1, "Pizza");
        controller.saveCategory(category);
        Mockito.verify(categoryService).save(category);
    }

    @Test
    public void testSaveDish() throws SQLException {
        Dish dish = new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true);

        controller.saveDish(dish);
        Mockito.verify(dishService).save(dish);
    }

    @Test
    public void testSaveCustomer() throws SQLException {
        Customer customer = new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null);

        controller.saveCustomer(customer);
        Mockito.verify(userService).save(customer);
    }

    @Test
    public void testSaveOrder() throws SQLException {
        Date date = new Date();

        Order order = new Order(
                List.of(new OrderPosition(1,
                        new Dish(1, "Spaghetti",
                                List.of(new Category(1, "Nudeln")),
                                2.0,
                                true),1)),
                date,
                new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null),
                0);
        controller.saveOrder(order);
        Mockito.verify(orderService).save(order);
    }

}
