package sample.GUI;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TableView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import sample.data_logic.UserSessionSingleton;
import sample.data_logic.dao.OrderDAO;
import sample.data_logic.dto.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class OrderHistoryModalControllerTest {
/*
    @Mock
    OrderDAO orderDAO;

    OrderHistoryModalController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new OrderHistoryModalController(orderDAO);
        new JFXPanel(); //to get JavaFX environment running
        UserSessionSingleton.currentSession().setUser( new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345"));

    }

    @Test
    public void displayDishes() throws SQLException {
        controller.table_order = new TableView<>();
        List<Order> dbOrders = List.of(
                new Order(1,
                        List.of(new OrderPosition(1, new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true),2)),
                        new Date(),
                        new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345")
                        ,
                        0.0)
        );

        Mockito.when(controller.orderDAO.getAllByUserId(1)).thenReturn(dbOrders);
        controller.displayDishes();

        assertEquals(1, controller.table_order.getItems().size());
    }
 */
}