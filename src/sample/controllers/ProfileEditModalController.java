package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import sample.dao.UserDAO;
import sample.dto.Admin;
import sample.dto.Customer;
import sample.dto.User;
import sample.dto.UserSession;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileEditModalController implements Initializable {
    @FXML
    private RadioButton t_m;
    @FXML
    private RadioButton t_f;
    @FXML
    private RadioButton t_d;
    @FXML
    private TextField fname_input;
    @FXML
    private TextField lname_input;
    @FXML
    private TextField street_input;
    @FXML
    private TextField hnumber_input;
    @FXML
    private TextField plz_input;
    @FXML
    private TextField email_input;
    @FXML
    private TextField password_input;
    @FXML
    private ToggleGroup gender_group;
    @FXML
    private Label error_msg;

    private ModalStatus status = ModalStatus.INITIALIZED;

    UserDAO userDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userDAO = new UserDAO();

        setUserData(UserSession.currentSession().getUser());
    }

    public void setUserData(User u) {
        gender_group.selectToggle(u.getGender() == 'f' ? t_f : u.getGender() == 'd' ? t_d : t_m);
        fname_input.setText(u.getFirstName());
        lname_input.setText(u.getLastname());
        email_input.setText(u.getEmail());
        password_input.setText(u.getPassword());

        if (u.getClass() == Customer.class) {
            Customer c = (Customer) u;
            street_input.setText(c.getAddress().getStreet());
            hnumber_input.setText(c.getAddress().getHouseNumber());
            plz_input.setText("" + c.getAddress().getZipCode());
        }
    }

    public void save(ActionEvent actionEvent) {
        if (!isFormValid()) {
            error_msg.setVisible(true);
            return;
        }
        error_msg.setVisible(false);

        if (UserSession.currentSession().getUser().getClass() == Customer.class)
            userDAO.update(UserSession.currentSession().getUser().getId(),
                    new Customer(
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
        else
            userDAO.update(UserSession.currentSession().getUser().getId(),
                    new Admin(
                            fname_input.getText(),
                            lname_input.getText(),
                            ((String) gender_group.getSelectedToggle().getUserData()).charAt(0),
                            email_input.getText(),
                            password_input.getText()
                    ));
        UserSession.currentSession().setUser(userDAO.get(UserSession.currentSession().getUser().getId()).get());
        status = ModalStatus.SUCCESS;
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    public void cancel(ActionEvent actionEvent) {
        status = ModalStatus.CLOSED;
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    private boolean isFormValid() {
        return fname_input.getText().length() > 0 &&
                lname_input.getText().length() > 0 &&
                email_input.getText().length() > 0 &&
                email_input.getText().contains("@") &&
                password_input.getText().length() > 0 &&
                ((UserSession.currentSession().getUser().getClass() == Customer.class &&
                        street_input.getText().length() > 0 &&
                        hnumber_input.getText().length() > 0 &&
                        plz_input.getText().length() > 0) ||
                        UserSession.currentSession().getUser().getClass() == Admin.class);
    }

    public ModalStatus getStatus() {
        return status;
    }
}
