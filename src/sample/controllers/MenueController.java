package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sample.dao.DishDAO;
import sample.dto.Dish;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MenueController implements Initializable {
    @FXML private VBox dishlist;

    public void synchronizeDishlist() {
       //TODO
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        synchronizeDishlist();
    }
}
