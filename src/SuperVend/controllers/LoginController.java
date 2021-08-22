package SuperVend.controllers;

import SuperVend.Main;
import SuperVend.model.ResourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import SuperVend.model.Security;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private ImageView logoImg;
    @FXML
    private TextField userTF;
    @FXML
    private PasswordField pwdTF;
    @FXML
    private Label errorMsgLabel;

    private Stage window;

    @FXML
    public void loginAction(ActionEvent e) {
        String username = userTF.getText();
        String password = pwdTF.getText();
        userTF.clear();
        pwdTF.clear();
        errorMsgLabel.setVisible(false);
        boolean result = Security.authenticate(username, password);
        if (!result) {
            errorMsgLabel.setVisible(true);
            errorMsgLabel.setText("Invalid username or password!");
            return;
        }
        window.hide();
        if (Security.isAdmin()) {
            //todo: admin screen
        } else {
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/customer.fxml")));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("SuperVend");
                stage.showAndWait();
                window.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoImg.setImage(new Image(ResourceManager.readFile("imgs/logo.png")));
    }

    public void setWindow(Stage stage) {
        window = stage;
    }
}
