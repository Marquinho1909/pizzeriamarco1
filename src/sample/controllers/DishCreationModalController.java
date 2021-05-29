package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.AlertService;
import sample.DAOFactory;
import sample.dao.CategoryDAO;
import sample.dao.DishDAO;
import sample.dto.Category;
import sample.dto.Dish;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DishCreationModalController extends ModalController implements Initializable {
    public Spinner<Double> price_input;
    public TextField name_input;
    public Label error_msg;
    public VBox categories_list;
    public TextField category_input;
    private List<Category> categories;

    CategoryDAO categoryDAO;
    DishDAO dishDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryDAO = (CategoryDAO) DAOFactory.getInstance().getDAO("Category");
        dishDAO = (DishDAO) DAOFactory.getInstance().getDAO("Dish");

        error_msg.setVisible(false);
        price_input.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 20, 1.0, 0.1));
        try {
            displayCategories();
        } catch (SQLException throwables) {
            setStatus(ModalStatus.FAILURE);
            AlertService.showAlert(Alert.AlertType.ERROR, "Fehler", "Ein Fehler ist aufgetreten, bitte wenden Sie sich an den Support.", ButtonType.OK);
        }
    }

    /**
     * saves input as dish if input is valid
     * @param actionEvent ae
     * @throws SQLException sql exception
     */
    public void save(ActionEvent actionEvent) throws SQLException {
        if (name_input.getText().equals("") || price_input.getValue() <= 0) {
            error_msg.setText("Bitte alle Felder ausfüllen");
            error_msg.setVisible(true);
            return;
        }
        error_msg.setVisible(false);

        List<Category> checked_categories = new ArrayList<>();
        for (Node n : categories_list.getChildren()) {
            CheckBox cb = (CheckBox) n;
            if (cb.isSelected())
                checked_categories.add((Category) cb.getUserData());
        }
        dishDAO.save(new Dish(name_input.getText(), checked_categories, price_input.getValue()));
        setStatus(ModalStatus.SUCCESS);
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    /**
     * closes modal when cancelled by user
     * @param actionEvent ae
     */
    public void cancel(ActionEvent actionEvent) {
        setStatus(ModalStatus.CLOSED);
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    /**
     * displays all categories in list
     * @throws SQLException sql exception
     */
    private void displayCategories() throws SQLException {
        categories_list.getChildren().clear();
        categories = categoryDAO.getAll();

        for (Category c : categories) {
            CheckBox cb = new CheckBox(c.getName());
            cb.setUserData(c);
            cb.setPrefWidth(150);
            VBox.setMargin(cb, new Insets(4, 0, 0, 5));
            categories_list.getChildren().add(cb);
        }
    }

    /**
     * creates category from input if input is valid
     * @throws SQLException sql exception
     */
    public void createCategory() throws SQLException {
        String cInput = category_input.getText();
        if (cInput.equals("")) {
            error_msg.setText("Das Feld ist leer");
            error_msg.setVisible(true);
            return;
        }
        if (categories.stream().anyMatch(c -> c.getName().equals(cInput))) {
            error_msg.setText("Diese Kategorie existiert bereits");
            error_msg.setVisible(true);
            return;
        }
        error_msg.setVisible(false);
        categoryDAO.save(new Category(cInput));
        displayCategories();
    }
}
