package SuperVend.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import SuperVend.model.Security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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

    private Security auth;
    private final Path rootFP = Path.of(System.getProperty("user.dir"));

    @FXML
    public void loginAction(ActionEvent e) {
        String username = userTF.getText();
        String password = pwdTF.getText();
        Security.authenticate(username, password);
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
                Files.copy(getClass().getResourceAsStream("/imgs/logo.png"), imgPath);
                logoImg.setImage(new Image(new FileInputStream(img)));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
