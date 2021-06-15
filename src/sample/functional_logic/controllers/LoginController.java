package sample.functional_logic.controllers;

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
import sample.data_logic.DAOFactory;
import sample.data_logic.dao.UserDAO;
import sample.data_logic.dto.Admin;
import sample.data_logic.dto.Customer;
import sample.data_logic.dto.User;
import sample.data_logic.dto.UserSessionSingleton;
import sample.functional_logic.AlertService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private TextField email_input;
    @FXML private PasswordField password_input;
    @FXML private Label error_msg;

    UserDAO userDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userDAO = (UserDAO) DAOFactory.getInstance().getDAO("User");

        // TODO Can later be deleted, easier for development
        email_input.setText("admin2@dummy.com");
        password_input.setText("topsecret");
    }

    /**
     * tries to login user with input, sets UserSession and redirects to fitting scene
     * @param actionEvent ae
     */
    public void login(ActionEvent actionEvent) {
        try {
            Optional<User> user = userDAO.getUserByEmailAndPassword(email_input.getText(), password_input.getText());

            if (user.isPresent()) {
                error_msg.setVisible(false);
                UserSessionSingleton.currentSession().setUser(user.get());

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                if (user.get().getClass() == Customer.class)
                    stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../../resources/views/customer/customer_page.fxml")))));
                else if (user.get().getClass() == Admin.class)
                    stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../../resources/views/admin/admin_page.fxml")))));

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
     * @param actionEvent ae
     */
    public void changeSceneToRegister(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../../resources/views/register.fxml")))));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            AlertService.showError();
        }
    }
}
