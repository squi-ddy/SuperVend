package SuperVend.controllers;

import SuperVend.Main;
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

    private final Path rootFP = Path.of(System.getProperty("user.dir"));

    @FXML
    public void loginAction(ActionEvent e) {
        String username = userTF.getText();
        String password = pwdTF.getText();
        errorMsgLabel.setVisible(false);
        boolean result = Security.authenticate(username, password);
        if (!result) {
            errorMsgLabel.setVisible(true);
            errorMsgLabel.setText("Invalid username or password!");
        }
        logoImg.getScene().getWindow().hide();
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/customer.fxml")));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("SuperVend");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Path imgPath = rootFP.resolve(Path.of("imgs/logo.png"));
        File img = new File(String.valueOf(imgPath));
        if (img.exists()) {
            try {
                logoImg.setImage(new Image(new FileInputStream(img)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Files.createDirectories(imgPath.getParent());
                Files.copy(Objects.requireNonNull(getClass().getResourceAsStream("/imgs/logo.png")), imgPath);
                logoImg.setImage(new Image(new FileInputStream(img)));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
