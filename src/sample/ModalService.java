package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.controllers.ModalController;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Service for opening view as modal, only for those that have ModalController as Controller
 */
public class ModalService {

    /**
     * opens FXML file as modal and returns modalStatus as result of user action
     * @param main stage
     * @param loader FXMLLoader of view
     * @param root loader.load(), happens extern in case external controller needs root's controller first
     * @return result of user action as modalStatus of controller
     */
    public static ModalController.ModalStatus openModal(Stage main, FXMLLoader loader, Parent root) {
        ModalController controller = loader.getController();

        Stage modal = new Stage();
        modal.setScene(new Scene(root));
        modal.initOwner(main);
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.showAndWait();

        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("MODAL CLOSED WITH STATUS " + controller.getStatus());

        return controller.getStatus();
    }
}
