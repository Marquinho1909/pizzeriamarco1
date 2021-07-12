package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.GUI.GUIHandler;
import sample.functional_logic.Controller;
import sample.functional_logic.service.*;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        JDBCClient JDBC = new JDBCClient();
        JDBC.initializeDatabase();
        Controller controller = new Controller(
                new UserService(),
                new CategoryService(),
                new CouponService(),
                new OrderService(),
                new DishService()
        );
        GUIHandler guiHandler = new GUIHandler(controller, primaryStage);
        controller.start(guiHandler);
        guiHandler.start();
    }
}
