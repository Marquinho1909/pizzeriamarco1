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
import javafx.stage.Stage;
import sample.functional_logic.AlertService;
import sample.functional_logic.controllers.LoginController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginGUIController implements Initializable {
    @FXML
    private TextField email_input;
    @FXML
    private PasswordField password_input;
    @FXML
    private Label error_msg;

    LoginController controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new LoginController();
    }

    /**
     * tries to login user with input, sets UserSession and redirects to fitting scene
     *
     * @param actionEvent ae
     */
    public void login(ActionEvent actionEvent) {
        try {
            if (controller.login(email_input.getText(), password_input.getText())) {
                error_msg.setVisible(false);

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                if (controller.isUserCustomer())
                    stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../views/customer/customer_page.fxml")))));
                else if (controller.isUserAdmin())
                    stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../views/admin/admin_page.fxml")))));

                stage.centerOnScreen();
            } else
                error_msg.setVisible(true);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            AlertService.showError();
        }
    }

    /**
     * switches scene to register
     *
     * @param actionEvent ae
     */
    public void changeSceneToRegister(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../views/register.fxml")))));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            AlertService.showError();
        }
    }
}
