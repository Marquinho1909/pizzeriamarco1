package sample.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.GUI.controller.*;

import java.io.IOException;

/**
 * Class that handles navigation and modals
 */
public class Router {
    private final String path = "../../resources/views/";
    Stage stage;

    public Router(Stage stage) {
        this.stage = stage;
    }

    /**
     * navigates to given fcml file and sets controller
     * @param parentController controller
     * @param file of fxml
     * @throws IOException io exception
     */
    public void navigateTo(ParentController parentController, String file) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path + file));
        loader.setController(parentController);
        stage.setScene(new Scene(loader.load()));
        stage.centerOnScreen();
    }

    /**
     * opens modal, sets controller and returns modals status after closure
     * @param modal controller
     * @param file of fxml
     * @return status of modal
     * @throws IOException io exception
     */
    public Modal.ModalStatus openModal(Modal modal, String file) throws IOException {
        Stage modal_stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path + file));
        loader.setController(modal);
        Parent root = loader.load();

        modal_stage.setScene(new Scene(root));
        modal_stage.initOwner(stage);
        modal_stage.initModality(Modality.APPLICATION_MODAL);
        modal_stage.showAndWait();

        return modal.getStatus();
    }

}
