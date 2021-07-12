package sample.GUI;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import sample.data_logic.dao.CategoryDAO;
import sample.data_logic.dao.DishDAO;
import sample.data_logic.dto.Category;
import sample.data_logic.dto.Dish;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class DishCreationModalControllerTest {
/*
    @Mock
    CategoryDAO categoryDAO;
    @Mock
    DishDAO dishDAO;

    DishCreationModalController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = new DishCreationModalController(categoryDAO,dishDAO);
        new JFXPanel(); //to get JavaFX environment running
        controller.name_input = new TextField();
        controller.price_input = new Spinner<>();
        controller.error_msg = new Label();
        controller.categories_list = new VBox();
        controller.category_input = new TextField();

        List<Category> dbCategories = List.of(
                new Category(1, "Pizza"),
                new Category(2, "Nudeln")
        );
        Mockito.when(controller.categoryDAO.getAll()).thenReturn(dbCategories);
        controller.setCategories(dbCategories);
    }

    @Test
    public void save() throws SQLException {
        Mockito.when(controller.dishDAO.save(new Dish("Lasagne", List.of(), 0.1 , true))).thenReturn(3);
        controller.name_input.setText("Lasagne");
        controller.price_input.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 20, 1.0, 0.1));

        ActionEvent ae = Mockito.mock(ActionEvent.class);

        controller.save(ae);
        Mockito.verify(controller.dishDAO.save(new Dish("Lasagne", List.of(), 0.1 , true)));
        //TODO
    }

    @Test
    public void displayCategories() {
        controller.displayCategories();
        assertEquals(2, controller.categories_list.getChildren().size());
    }

    @Test
    public void createCategory() throws SQLException {
        controller.category_input.setText("Salat");
        controller.createCategory();

        Mockito.verify(controller.categoryDAO).save(new Category("Salat"));
    }
*/
}