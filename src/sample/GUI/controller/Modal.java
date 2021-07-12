package sample.GUI.controller;

import javafx.fxml.Initializable;
import sample.GUI.GUIHandler;
import sample.GUI.ParentController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller that is extended by every Controller of a Modal,
 * needed for ModalService and returning result as ModalStatus
 */
public abstract class Modal implements Initializable {
    private ModalStatus status = ModalStatus.INITIALIZED;
    public ModalStatus getStatus() {
        return status;
    }
    public void setStatus(ModalStatus status) {
        this.status = status;
    }
    public final GUIHandler guiHandler;
    public Modal(GUIHandler guiHandler) {
        this.guiHandler = guiHandler;
    }
    public abstract void initialize(URL url, ResourceBundle resourceBundle);

    /**
     * enum to identify the result of a modal
     */
    public enum ModalStatus {
        INITIALIZED, SUCCESS, FAILURE, CLOSED
    }
}

