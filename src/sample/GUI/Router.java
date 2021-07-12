package sample.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.GUI.controller.*;
import sample.GUI.controller.modal.DishCreationModalController;
import sample.GUI.controller.modal.OrderHistoryModalController;

import java.io.IOException;

public class Router {
    private final String path = "../../resources/views/";
    Stage stage;

    public Router(Stage stage) {
        this.stage = stage;
    }

    public void navigateTo(ParentController parentController, String file) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path + file));
        loader.setController(parentController);
        stage.setScene(new Scene(loader.load()));
        stage.centerOnScreen();
    }

    public Modal.ModalStatus openModal(Modal modal, String file) throws IOException {
        Stage modal_stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path + file));
        loader.setController(modal);
        Parent root = loader.load();

        //modal.setUp();
        modal_stage.setScene(new Scene(root));
        modal_stage.initOwner(stage);
        modal_stage.initModality(Modality.APPLICATION_MODAL);
        modal_stage.showAndWait();

        return modal.getStatus();
    }

    public Modal.ModalStatus openOrderHistoryModal(OrderHistoryModalController orderHistoryModalController) throws IOException {
        Stage modal = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path + "customer/customer_page.fxml"));
        loader.setController(orderHistoryModalController);
        modal.setScene(new Scene(loader.load()));
        modal.initOwner(stage);
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.showAndWait();

        return orderHistoryModalController.getStatus();
    }
}
