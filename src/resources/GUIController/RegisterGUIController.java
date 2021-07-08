package resources.GUIController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import sample.data_logic.DAOFactory;
import sample.data_logic.UserSessionSingleton;
import sample.data_logic.dao.UserDAO;
import sample.data_logic.dto.Customer;
import sample.functional_logic.AlertService;
import sample.functional_logic.controllers.RegisterController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterGUIController implements Initializable {
    @FXML private ToggleGroup gender_group;
    @FXML private TextField fname_input;
    @FXML private TextField lname_input;
    @FXML private TextField street_input;
    @FXML private TextField hnumber_input;
    @FXML private TextField plz_input;
    @FXML private TextField email_input;
    @FXML private PasswordField password_input;
    @FXML private Label error_msg;

    RegisterController controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new RegisterController();
    }

    /**
     * creates customer if form is valid and redirects to customer page
     * @param actionEvent ae
     */
    public void register(ActionEvent actionEvent) {
        try {
            if (isFormValid()) {
                error_msg.setVisible(false);
                controller.register(new Customer(
                        fname_input.getText(),
                        lname_input.getText(),
                        new Customer.Address(
                                street_input.getText(),
                                hnumber_input.getText(),
                                Integer.parseInt(plz_input.getText())
                        ),
                        ((String) gender_group.getSelectedToggle().getUserData()).charAt(0),
                        email_input.getText(),
                        password_input.getText()
                ));

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../views/customer/customer_page.fxml")))));
                stage.centerOnScreen();
            } else {
                error_msg.setVisible(true);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            AlertService.showError();
        }
    }

    /**
     * redirects to login page
     * @param actionEvent ae
     */
    public void returnToLoginPage(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../views/login.fxml")))));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * returns if input fields are valid
     * @return if all of input is valid
     */
    private boolean isFormValid() {
        return fname_input.getText().length() > 0 &&
                lname_input.getText().length() > 0 &&
                street_input.getText().length() > 0 &&
                hnumber_input.getText().length() > 0 &&
                plz_input.getText().length() > 0 &&
                email_input.getText().length() > 0 &&
                email_input.getText().contains("@") &&
                password_input.getText().length() > 0;
    }

}
