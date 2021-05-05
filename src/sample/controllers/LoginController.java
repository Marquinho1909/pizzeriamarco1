package sample.controllers;

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
import sample.dao.UserDAO;
import sample.dto.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private TextField email_input;
    @FXML private PasswordField password_input;
    @FXML private Label error_msg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO Can later be deleted, easier for development
        email_input.setText("admin2@dummy.com");
        password_input.setText("topsecret");
    }

    // TODO when admin, show admin page
    public void login(ActionEvent actionEvent) throws IOException {
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByEmailAndPassword(email_input.getText(), password_input.getText());

        if (user != null) {
            error_msg.setVisible(false);
            UserDAO.loggedInUser = user;

            System.out.println("Logged in User using " + email_input.getText() + " and " + password_input.getText() + " " + user);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../resources/views/menu.fxml"))));
            stage.setMaximized(true);
        }
        else
            error_msg.setVisible(true);
    }

    public void changeSceneToRegister(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../resources/views/register.fxml"))));
    }

}
