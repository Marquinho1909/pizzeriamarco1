package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        JDBCClient JDBC = new JDBCClient();
        JDBC.initializeDatabase();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../resources/views/login.fxml")));
        primaryStage.setTitle("Pizzeria Marco");
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(640);
        primaryStage.setScene(new Scene(root));
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
