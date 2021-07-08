package sample.functional_logic.controllers;

import java.util.Observable;

/**
 * Controller that is extended by every Controller of a Modal,
 * needed for ModalService and returning result as ModalStatus
 */
public class ModalController extends Observable {
    private ModalStatus status = ModalStatus.INITIALIZED;

    public ModalStatus getStatus() {
        return status;
    }

    public void setStatus(ModalStatus status) {
        this.status = status;
    }

    /**
     * enum to identify the result of a modal
     */
    public enum ModalStatus {
        INITIALIZED, SUCCESS, FAILURE, CLOSED
    }
}

