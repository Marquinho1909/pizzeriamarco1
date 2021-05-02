package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.dao.UserDAO;
import sample.dto.User;

import java.io.IOException;

public class LoginController {
    public TextField email_input;
    public PasswordField password_input;

    public void login(ActionEvent actionEvent) throws IOException {
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByEmailAndPassword(email_input.getText(), password_input.getText());
        if (user != null) {
            UserDAO.loggedInUser = user;
        }
        System.out.println("Logged in User using " + email_input.getText() + " and " + password_input.getText() + " " + user);

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../resources/views/menue.fxml"))));
    }

    public void changeSceneToRegister(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../resources/views/register.fxml"))));
    }
}
