package sample.GUI.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import sample.GUI.AlertService;
import sample.GUI.GUIHandler;
import sample.GUI.ParentController;
import sample.data_logic.dto.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController extends ParentController {
    @FXML private ToggleGroup gender_group;
    @FXML private TextField fname_input;
    @FXML private TextField lname_input;
    @FXML private TextField street_input;
    @FXML private TextField hnumber_input;
    @FXML private TextField plz_input;
    @FXML private TextField email_input;
    @FXML private PasswordField password_input;
    @FXML private Label error_msg;

    public RegisterController(GUIHandler guiHandler) {
        super(guiHandler);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * creates customer if form is valid and redirects to customer page
     */
    public void register() {
        try {
            if (isFormValid()) {
                error_msg.setVisible(false);
                guiHandler.register(new Customer(
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

                guiHandler.navigateToCustomerPage();
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
     */
    public void returnToLoginPage() {
        try {
            guiHandler.navigateToLoginPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * returns if input fields are valid
     *
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

    @Override
    public void update() {}
}
