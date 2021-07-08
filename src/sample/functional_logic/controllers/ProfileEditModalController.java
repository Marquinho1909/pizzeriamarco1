package sample.functional_logic.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import sample.data_logic.DAOFactory;
import sample.data_logic.dao.UserDAO;
import sample.data_logic.dto.Admin;
import sample.data_logic.dto.Customer;
import sample.data_logic.dto.User;
import sample.data_logic.UserSessionSingleton;
import sample.functional_logic.AlertService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProfileEditModalController extends ModalController implements Initializable {
    @FXML private RadioButton t_m;
    @FXML private RadioButton t_f;
    @FXML private RadioButton t_d;
    @FXML private TextField fname_input;
    @FXML private TextField lname_input;
    @FXML private TextField street_input;
    @FXML private TextField hnumber_input;
    @FXML private TextField plz_input;
    @FXML private TextField email_input;
    @FXML private TextField password_input;
    @FXML private ToggleGroup gender_group;
    @FXML private Label error_msg;

    UserDAO userDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userDAO = (UserDAO) DAOFactory.getInstance().getDAO("User");

        setUserData(UserSessionSingleton.currentSession().getUser());
    }

    /**
     * fills input fields with user data
     * @param u user for information
     */
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

    /**
     * saves changes for Admin or Customer
     * notifies AdminPage- / CustomerPage-Observer with new name to update menu button on page
     * @param actionEvent ae
     */
    public void save(ActionEvent actionEvent) {
        if (!isFormValid()) {
            error_msg.setVisible(true);
            return;
        }
        error_msg.setVisible(false);

        try {
            if (UserSessionSingleton.currentSession().getUser().getClass() == Customer.class)
                userDAO.update(UserSessionSingleton.currentSession().getUser().getId(),
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
                userDAO.update(UserSessionSingleton.currentSession().getUser().getId(),
                        new Admin(
                                fname_input.getText(),
                                lname_input.getText(),
                                ((String) gender_group.getSelectedToggle().getUserData()).charAt(0),
                                email_input.getText(),
                                password_input.getText()
                        ));
            UserSessionSingleton.currentSession().setUser(userDAO.get(UserSessionSingleton.currentSession().getUser().getId()).orElseThrow());
            setStatus(ModalStatus.SUCCESS);
            setChanged();
            notifyObservers(lname_input.getText() + ", " + fname_input.getText());
        } catch (SQLException e) {
            e.printStackTrace();
            setStatus(ModalStatus.FAILURE);
            AlertService.showError();
        }
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    /**
     * closes modal when cancelled by user
     * @param actionEvent ae
     */
    public void cancel(ActionEvent actionEvent) {
        setStatus(ModalStatus.CLOSED);
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    /**
     * checks if input of form is valid
     * @return if all of input is valid
     */
    private boolean isFormValid() {
        return fname_input.getText().length() > 0 &&
                lname_input.getText().length() > 0 &&
                email_input.getText().length() > 0 &&
                email_input.getText().contains("@") &&
                password_input.getText().length() > 0 &&
                ((UserSessionSingleton.currentSession().getUser().getClass() == Customer.class &&
                        street_input.getText().length() > 0 &&
                        hnumber_input.getText().length() > 0 &&
                        plz_input.getText().length() > 0) ||
                        UserSessionSingleton.currentSession().getUser().getClass() == Admin.class);
    }

}
