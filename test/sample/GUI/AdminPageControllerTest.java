package sample.GUI;

import javafx.scene.control.TableView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import sample.data_logic.UserSessionSingleton;
import sample.data_logic.dao.*;
import sample.data_logic.dto.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class AdminPageControllerTest {
/*
    @Mock
    private UserDAO userDAO;
    @Mock
    private OrderDAO orderDAO;
    @Mock
    private DishDAO dishDAO;

    AdminPageService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new AdminPageService();
        service.dishDAO = dishDAO;
        service.userDAO = userDAO;
        service.orderDAO = orderDAO;

        UserSessionSingleton.currentSession().setUser(new Admin("Addi", "Dummy", 'm', "itzAddiDummy@yahoo.com", "12345"));
    }

    @Test()
    public void makeCustomerAdmin() throws SQLException {
        service.makeCustomerAdmin(1);

        Mockito.verify(service.userDAO).makeCustomerAdmin(1);
    }

    @Test
    public void displayCustomers() throws SQLException {
        List<Customer> dbCustomers = List.of(
                new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345"),
                new Customer(2, "Max", "Mustermann", new Customer.Address("Dummy Straße", "25", 12345), 'f', "maxiBoi@web.de", "12345")
        );

        Mockito.when(controller.userDAO.getAllCustomers()).thenReturn(dbCustomers);

        controller.table_user = new TableView<>();
        controller.displayCustomers();

        assertEquals(2, controller.table_user.getItems().size());
    }

    @Test
    public void displayDishes() throws SQLException {
        List<Dish> dbDishes = List.of(
                new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true),
                new Dish(2, "Makkaroni", List.of(new Category(1, "Nudeln")), 3.0,true)
        );

        Mockito.when(controller.dishDAO.getAll()).thenReturn(dbDishes);

        controller.table_dish = new TableView<>();
        controller.displayDishes();

        assertEquals(2, controller.table_dish.getItems().size());
    }

    @Test
    public void displayOrders() throws SQLException {
        List<Order> dbOrders = List.of(
                new Order(1,
                        List.of(new OrderPosition(1, new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true),2)),
                        new Date(),
                        new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345")
                        ,
                        0.0)
        );

        Mockito.when(controller.orderDAO.getAll()).thenReturn(dbOrders);

        controller.table_order = new TableView<>();
        controller.displayOrders();

        assertEquals(1, controller.table_order.getItems().size());
    }

    @Test
    public void deleteDish() throws SQLException {
//        List<Dish> dbDish = List.of(
//                new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true),
//                new Dish(2, "Makkaroni", List.of(new Category(1, "Nudeln")), 3.0,true)
//        );
//
//        Mockito.when(controller.dishDAO.getAll()).thenReturn(dbDish);
//
//        controller.table_dish = new TableView<>();
//        controller.displayDishes();
//
//        controller.table_dish.getSelectionModel().select(0);
//        controller.deleteDish();
//        controller.displayDishes();
//        assertEquals(controller.table_dish.getItems().size(), 1);
//        //Mockito.verify(controller.dishDAO).delete(1);
    }

    @Test
    public void deleteOrderHistory() {
    }

    @Test
    public void changeDishActivation() {
    }
*/
}