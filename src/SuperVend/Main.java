package SuperVend;

import SuperVend.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    // global settings
    public static final double verNo = 1.1;
    public static final int cartLimit = 10;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("view/login.fxml")));
        Parent root = loader.load();
        ((LoginController)loader.getController()).setWindow(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("SuperVend Login");
        stage.setResizable(false);
        stage.show();
    }
}
