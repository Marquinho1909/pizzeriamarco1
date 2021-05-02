package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import sample.dao.UserDAO;
import sample.dto.Customer;

import java.io.IOException;

public class RegisterController {
    public ToggleGroup gender_group;
    public TextField fname_input;
    public TextField lname_input;
    public TextField street_input;
    public TextField hnumber_input;
    public TextField plz_input;
    public TextField email_input;
    public PasswordField password_input;

    UserDAO userDAO = new UserDAO();

    public void register(ActionEvent actionEvent) throws IOException {
        System.out.println("TRYING REGISTERING WITH: " + fname_input.getText() + ", " + lname_input.getText() + ", " + email_input.getText() + ", " + password_input.getText());
        if (isFormValid()) {
            userDAO.save(new Customer(
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
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../resources/views/menue.fxml"))));
        }
    }

    public boolean isFormValid() {
        return fname_input.getText().length() > 0 &&
                lname_input.getText().length() > 0 &&
                street_input.getText().length() > 0 &&
                hnumber_input.getText().length() > 0 &&
                plz_input.getText().length() > 0 &&
                email_input.getText().length() > 0 &&
                password_input.getText().length() > 0;
    }
}
