package sample.GUI;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import sample.data_logic.UserSessionSingleton;
import sample.data_logic.dao.CategoryDAO;
import sample.data_logic.dao.DishDAO;
import sample.data_logic.dto.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class CustomerPageControllerTest {
/*
    @Mock
    DishDAO dishDAO;
    @Mock
    CategoryDAO categoryDAO;

    CustomerPageController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new CustomerPageController(dishDAO, categoryDAO);
        new JFXPanel(); //to get JavaFX environment running
        UserSessionSingleton.currentSession().setUser( new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345"));

        List<Category> dbCategories = List.of(
                new Category(1, "Pizza"),
                new Category(2, "Nudeln")
        );
        controller.setCategories(dbCategories);

        List<Dish> dbDishes = List.of(
                new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true),
                new Dish(2, "Makkaroni", List.of(new Category(1, "Nudeln")), 3.0,true)
        );
        controller.setDishes(dbDishes);

        List<OrderPosition> dbOrderPositions = List.of(new OrderPosition(1, new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true), 2));
        controller.setDishesInCart(dbOrderPositions);

        controller.categories_cb = new ComboBox<>();
        controller.dishlist = new VBox();
        controller.categories_cb = new ComboBox<>();
        controller.cart = new VBox();
        controller.lTotal = new Label();
    }

    @Test
    public void displayCategories() {
        controller.displayCategories();
        assertEquals(3, controller.categories_cb.getItems().size());
    }

    @Test
    public void displayDishes() {
        controller.displayCategories();
        controller.displayDishes();

        //two dishes + two categories
        assertEquals(4, controller.dishlist.getChildren().size());
    }

    @Test
    public void displayCartAndCalculateTotal() {
        controller.displayCart();
        assertEquals(1, controller.cart.getChildren().size());
        assertEquals("Total: 4,00€", controller.lTotal.getText());
    }
*/
}