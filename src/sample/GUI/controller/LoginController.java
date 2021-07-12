package sample.GUI.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.GUI.AlertService;
import sample.GUI.GUIHandler;
import sample.GUI.ParentController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends ParentController {
    @FXML private TextField email_input;
    @FXML private PasswordField password_input;
    @FXML private Label error_msg;

    public LoginController(GUIHandler guiHandler) {
        super(guiHandler);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * tries to login user with input, sets UserSession and redirects to fitting scene
     */
    public void login() {
        try {
            if (guiHandler.login(email_input.getText(), password_input.getText())) {
                error_msg.setVisible(false);
                if (guiHandler.isUserCustomer())
                    guiHandler.navigateToCustomerPage();
                else if (guiHandler.isUserAdmin())
                    guiHandler.navigateToAdminPage();
            } else
                error_msg.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            AlertService.showError();
        }
    }

    /**
     * switches scene to register
     */
    public void changeSceneToRegister() {
        try {
            guiHandler.navigateToRegisterPage();
        } catch (IOException e) {
            e.printStackTrace();
            AlertService.showError();
        }
    }

    @Override
    public void update() {}
}
