package resources.GUIController;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.data_logic.dto.Category;
import sample.data_logic.dto.Dish;
import sample.functional_logic.AlertService;
import sample.functional_logic.controllers.DishCreationModalController;
import sample.functional_logic.controllers.ModalController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DishCreationModalGUIController extends ModalController implements Initializable {
    public Spinner<Double> price_input;
    public TextField name_input;
    public Label error_msg;
    public VBox categories_list;
    public TextField category_input;

    DishCreationModalController controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new DishCreationModalController();

        error_msg.setVisible(false);
        price_input.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 20, 1.0, 0.1));

        displayCategories();
    }

    /**
     * saves input as dish if input is valid
     *
     * @param actionEvent ae
     */
    public void save(ActionEvent actionEvent) {
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
        try {
            controller.saveDish(new Dish(name_input.getText(), checked_categories, price_input.getValue(), true));
            setStatus(ModalStatus.SUCCESS);
        } catch (SQLException e) {
            e.printStackTrace();
            setStatus(ModalStatus.FAILURE);
            AlertService.showError();
        }
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    /**
     * closes modal when cancelled by user
     *
     * @param actionEvent ae
     */
    public void cancel(ActionEvent actionEvent) {
        setStatus(ModalStatus.CLOSED);
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    /**
     * displays all categories in list
     */
    public void displayCategories() {
        categories_list.getChildren().clear();
        try {
            for (Category c : controller.getCategories()) {
                CheckBox cb = new CheckBox(c.getName());
                cb.setUserData(c);
                cb.setPrefWidth(150);
                VBox.setMargin(cb, new Insets(4, 0, 0, 5));
                categories_list.getChildren().add(cb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertService.showError();
        }
    }

    /**
     * creates category from input if input is valid
     */
    public void createCategory() {
        String cInput = category_input.getText();
        if (cInput.equals("")) {
            error_msg.setText("Das Feld ist leer");
            error_msg.setVisible(true);
            return;
        }
        try {
            if (controller.doesCategoryExist(cInput)) {
                error_msg.setText("Diese Kategorie existiert bereits");
                error_msg.setVisible(true);
                return;
            }
            error_msg.setVisible(false);

            controller.saveCategory(new Category(cInput));
            displayCategories();
        } catch (SQLException e) {
            e.printStackTrace();
            AlertService.showError();
        }
    }

    public void deleteCategories() {
        List<Category> checked_categories = new ArrayList<>();
        for (Node n : categories_list.getChildren()) {
            CheckBox cb = (CheckBox) n;
            if (cb.isSelected())
                checked_categories.add((Category) cb.getUserData());
        }
        try {
            if (!controller.canAllCategoriesBeDeleted(checked_categories)) {
                AlertService.showAlert(Alert.AlertType.ERROR, "Fehlgeschlagen", "Mindestens eines der ausgewählten Kategorien hat Gerichte zugeordnet. Löschen Sie diese und versuchen Sie es nochmal", ButtonType.OK);
                return;
            }

            StringBuilder content = new StringBuilder("Folgende Kategorien werden gelöscht: ");
            for (Category c : checked_categories)
                content.append(c.getName()).append(", ");
            ButtonType result = AlertService.showAlert(Alert.AlertType.CONFIRMATION, "Kategorien löschen", content.substring(0, content.length() - 2), ButtonType.YES, ButtonType.CANCEL);

            if (result.equals(ButtonType.YES)) {
                controller.deleteCategories(checked_categories);
                displayCategories();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertService.showError();
        }
    }

}