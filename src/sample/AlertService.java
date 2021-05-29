package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertService {

    public static ButtonType showAlert(Alert.AlertType type, String title, String content, ButtonType... buttons) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.getButtonTypes().setAll(buttons);
        alert.showAndWait();

        return alert.getResult();
    }
}
