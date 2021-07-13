package sample.GUI;

import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import sample.GUI.controller.AdminPageController;
import sample.GUI.controller.CustomerPageController;
import sample.GUI.controller.LoginController;
import sample.GUI.controller.RegisterController;
import sample.GUI.controller.modal.DishCreationModalController;
import sample.GUI.controller.modal.OrderHistoryModalController;
import sample.GUI.controller.modal.OrderModalController;
import sample.GUI.controller.modal.ProfileEditModalController;
import sample.data_logic.UserSessionSingleton;
import sample.data_logic.dto.*;
import sample.functional_logic.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class GUIHandlerTest {
    @Mock
    Router router;
    @Mock
    Controller controller;
    @Mock
    AdminPageController adminPageController;
    @Mock
    CustomerPageController customerPageController;
    @Mock
    LoginController loginController;
    @Mock
    OrderModalController orderModalController;
    @Mock
    RegisterController registerController;
    @Mock
    DishCreationModalController dishCreationModalController;
    @Mock
    OrderHistoryModalController orderHistoryModalController;
    @Mock
    ProfileEditModalController profileEditModalController;
    @Mock
    Stage stage;

    GUIHandler guiHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        guiHandler = new GUIHandler(controller, stage);
        guiHandler.router = router;
        guiHandler.adminPageController = adminPageController;
        guiHandler.customerPageController = customerPageController;
        guiHandler.loginController = loginController;
        guiHandler.orderModalController = orderModalController;
        guiHandler.registerController = registerController;
        guiHandler.dishCreationModalController = dishCreationModalController;
        guiHandler.orderHistoryModalController = orderHistoryModalController;
        guiHandler.profileEditModalController = profileEditModalController;
    }

    @Test
    public void testLogout() {
        guiHandler.logout();
        Assert.assertNull(UserSessionSingleton.currentSession().getUser());
    }

    @Test
    public void testRegister() throws SQLException {
        Customer customer = new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null);

        guiHandler.register(customer);
        Mockito.verify(controller).saveCustomer(customer);
        assertEquals(customer, UserSessionSingleton.currentSession().getUser());
    }

    @Test
    public void testLogin() {
        UserSessionSingleton.currentSession().cleanUserSession();
        Assert.assertNull(UserSessionSingleton.currentSession().getUser());
        Mockito.when(guiHandler.getUsers()).thenReturn(List.of(new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null)));

        assertTrue(guiHandler.login("itzDebbyDummy@yahoo.com", "12345"));
        assertEquals(1, UserSessionSingleton.currentSession().getUser().getId());
    }

    @Test
    public void testCanDishBeDeleted() {
        List<Order> dbOrders = List.of(
                new Order(1,
                        List.of(new OrderPosition(1, new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true),2)),
                        new Date(),
                        new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null)
                        , 0.0)
        );
        Mockito.when(guiHandler.getOrders()).thenReturn(dbOrders);
        assertFalse(guiHandler.canDishBeDeleted(1));
    }

    @Test
    public void testGetCouponOfLoggedInUser() {
        UserSessionSingleton.currentSession().setUser(new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", new Coupon(1, 0.3)));
        Mockito.when(controller.isUserCustomer()).thenCallRealMethod();
        Assert.assertEquals(1, guiHandler.getCouponOfLoggedInUser().getId());
    }

    @Test
    public void testOrder() throws SQLException {
        UserSessionSingleton.currentSession().setUser(new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null));

        guiHandler.addOrderToCart(new OrderPosition(1,
                new Dish(1, "Spaghetti",
                        List.of(new Category(1, "Nudeln")),
                        2.0,
                        true),1));

        Date date = new Date();
        guiHandler.order(false, date);

        Mockito.verify(controller).saveOrder(
                new Order(
                        List.of(new OrderPosition(1,
                                new Dish(1, "Spaghetti",
                                        List.of(new Category(1, "Nudeln")),
                                        2.0,
                                        true),1)),
                        date,
                        new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null),
                        0)
        );
    }

    @Test
    public void testLoadLoggedInUser() {
        guiHandler.loadLoggedInUser();
        Mockito.verify(controller).loadLoggedInUser();
    }

    @Test
    public void testDoesCategoryExist() {
        List<Category> dbCategories = List.of(
                new Category(1, "Pizza"),
                new Category(2, "Nudeln")
        );
        Mockito.when(guiHandler.getCategories()).thenReturn(dbCategories);

        assertTrue(guiHandler.doesCategoryExist("Pizza"));
        assertFalse(guiHandler.doesCategoryExist("Lasagne"));
    }

    @Test
    public void testIsUserCustomer() {
        UserSessionSingleton.currentSession().setUser(new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null));
        Mockito.when(controller.isUserCustomer()).thenCallRealMethod();

        assertTrue(guiHandler.isUserCustomer());
    }

    @Test
    public void testIsUserAdmin() {
        UserSessionSingleton.currentSession().setUser(new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null));
        Mockito.when(controller.isUserAdmin()).thenCallRealMethod();

        assertFalse(guiHandler.isUserAdmin());
    }

    @Test
    public void testCanAllCategoriesBeDeleted() {
        List<Dish> dbDishes = List.of(
                new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true),
                new Dish(2, "Makkaroni", List.of(new Category(1, "Nudeln")), 3.0,true)
        );

        Mockito.when(controller.getDishes()).thenReturn(dbDishes);
        assertFalse(guiHandler.canAllCategoriesBeDeleted(List.of(
                new Category(1, "Pizza"),
                new Category(2, "Nudeln")
        )));
        assertTrue(guiHandler.canAllCategoriesBeDeleted(List.of(
                new Category(1, "Pizza"),
                new Category(2, "Lasagne")
        )));
    }

    @Test
    public void testAddedToExistingOrder() {
        guiHandler.addOrderToCart(new OrderPosition(1,
                new Dish(1, "Spaghetti",
                        List.of(new Category(1, "Nudeln")),
                        2.0,
                        true),1));

        assertTrue(guiHandler.addedToExistingOrder(new Dish(1, "Spaghetti",
                List.of(new Category(1, "Nudeln")),
                2.0,
                true),1));
        assertEquals(1, guiHandler.getCart().size());
        assertEquals(2, guiHandler.getCart().get(0).getAmount());
    }

    @Test
    public void testGetOrdersFromLoggedInUser() {
        UserSessionSingleton.currentSession().setUser(new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null));

        List<Order> dbOrders = List.of(
                new Order(1,
                        List.of(new OrderPosition(1, new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true),2)),
                        new Date(),
                        new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null)
                        ,
                        0.0)
        );

        Mockito.when(controller.getOrders()).thenReturn(dbOrders);
        assertEquals(1, guiHandler.getOrdersFromLoggedInUser().size());
        UserSessionSingleton.currentSession().setUser(new Customer(2, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null));
        assertEquals(0, guiHandler.getOrdersFromLoggedInUser().size());
    }

    @Test
    public void testSetDishActivation() throws SQLException {
        List<Dish> dbDishes = List.of(
                new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true),
                new Dish(2, "Makkaroni", List.of(new Category(1, "Nudeln")), 3.0,true)
        );

        Mockito.when(controller.getDishes()).thenReturn(dbDishes);
        guiHandler.setDishActivation(1);
        Mockito.verify(controller).updateDish(1, new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,false));
    }

    @Test
    public void testMakeCustomerAdmin() throws SQLException {
        Mockito.when(guiHandler.getUsers()).thenReturn(List.of(new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null)));
        guiHandler.makeCustomerAdmin(1);
        Mockito.verify(controller).updateUser(1, new Admin(1, "Debby", "Dummy", 'f', "itzDebbyDummy@yahoo.com", "12345"));
    }

    @Test
    public void testAddOrderToCart() {
        guiHandler.addOrderToCart(
                new OrderPosition(
                        1,
                        new Dish(1,
                                "Spaghetti",
                                List.of(new Category(1, "Nudeln")),
                                2.0,
                                true),
                        2));

        assertEquals(1, guiHandler.getCart().size());
    }

    @Test
    public void testClearCart() {
        guiHandler.addOrderToCart(
                new OrderPosition(
                        1,
                        new Dish(1,
                                "Spaghetti",
                                List.of(new Category(1, "Nudeln")),
                                2.0,
                                true),
                        2));
        guiHandler.clearCart();
        assertEquals(0, guiHandler.getCart().size());
    }

    @Test
    public void testRemoveOrderFromCart() {
        guiHandler.addOrderToCart(
                new OrderPosition(
                        1,
                        new Dish(1,
                                "Spaghetti",
                                List.of(new Category(1, "Nudeln")),
                                2.0,
                                true),
                        2));
        guiHandler.removeOrderFromCart(1);
        assertEquals(0, guiHandler.getCart().size());
    }

    @Test
    public void testDeleteOrderHistory() throws SQLException {
        guiHandler.deleteOrderHistory();
        Mockito.verify(controller).deleteOrderHistory();
    }

    @Test
    public void testDeleteDish() throws SQLException {
        guiHandler.deleteDish(1);
        Mockito.verify(controller).deleteDish(1);
    }

    @Test
    public void testDeleteCategories() throws SQLException {
        guiHandler.deleteCategories(List.of(new Category(1, "Pizza")));
        Mockito.verify(controller).deleteCategories(List.of(new Category(1, "Pizza")));
    }

    @Test
    public void testSaveCategory() throws SQLException {
        guiHandler.saveCategory(new Category("Salat"));
        Mockito.verify(controller).saveCategory(new Category("Salat"));
    }

    @Test
    public void testSaveDish() throws SQLException {
        guiHandler.saveDish(new Dish("Lasagne", List.of(), 0.1 , true));
        Mockito.verify(controller).saveDish(new Dish("Lasagne", List.of(), 0.1 , true));
    }

    @Test
    public void testUpdateProfile() throws SQLException {
        UserSessionSingleton.currentSession().setUser( new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345", null));
        guiHandler.updateProfile(1, new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "123456", null));
        Mockito.verify(controller).updateProfile(1, new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "123456", null));
    }

    @Test
    public void testNavigateToRegisterPage() throws IOException {
        guiHandler.navigateToRegisterPage();
        Mockito.verify(router).navigateTo(registerController, "register.fxml");
    }

    @Test
    public void testNavigateToLoginPage() throws IOException {
        guiHandler.navigateToLoginPage();
        Mockito.verify(router).navigateTo(loginController, "login.fxml");
    }

    @Test
    public void testNavigateToAdminPage() throws IOException {
        guiHandler.navigateToAdminPage();
        Mockito.verify(router).navigateTo(adminPageController, "admin/admin_page.fxml");
    }

    @Test
    public void testNavigateToCustomerPage() throws IOException {
        guiHandler.navigateToCustomerPage();
        Mockito.verify(router).navigateTo(customerPageController, "customer/customer_page.fxml");
    }

    @Test
    public void testOpenDishCreationModal() throws IOException {
        guiHandler.openDishCreationModal();
        Mockito.verify(router).openModal(dishCreationModalController, "admin/dish_creation_modal.fxml");
    }

    @Test
    public void testOpenAdminProfileEditModal() throws IOException {
        guiHandler.openAdminProfileEditModal();
        Mockito.verify(router).openModal(profileEditModalController, "admin/admin_profile_edit_modal.fxml");
    }

    @Test
    public void testOpenCustomerProfileEditModal() throws IOException {
        guiHandler.openCustomerProfileEditModal();
        Mockito.verify(router).openModal(profileEditModalController, "customer/customer_profile_edit_modal.fxml");
    }

    @Test
    public void testOpenOrderHistoryModal() throws IOException {
        guiHandler.openOrderHistoryModal();
        Mockito.verify(router).openModal(orderHistoryModalController, "customer/order_history_modal.fxml");
    }

    @Test
    public void testOpenOrderModal() throws IOException {
        guiHandler.openOrderModal();
        Mockito.verify(router).openModal(orderModalController, "customer/order_modal.fxml");
    }
}
