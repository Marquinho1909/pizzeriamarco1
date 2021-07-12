package sample.GUI;

import javafx.fxml.Initializable;

public abstract class ParentController implements Initializable {
    public final GUIHandler guiHandler;

    public ParentController(GUIHandler guiHandler) {
        this.guiHandler = guiHandler;
    }

    public abstract void update();
}
