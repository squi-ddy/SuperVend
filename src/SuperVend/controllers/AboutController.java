package SuperVend.controllers;

import SuperVend.model.Advertisement;
import SuperVend.model.ResourceManager;
import SuperVend.model.Security;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AboutController implements Initializable {
    @FXML
    private MediaView advBox;
    @FXML
    private ImageView logoImg;
    @FXML
    private Label adminInfoLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!Security.isAdmin()) {
            adminInfoLabel.setManaged(false);
            Advertisement.reset();
            advBox.setMediaPlayer(createPlayer());
        } else {
            adminInfoLabel.setVisible(true);
            advBox.setVisible(false);
            advBox.setManaged(false);
        }
        logoImg.setImage(new Image(ResourceManager.readFile("imgs/logo.png")));
    }

    public void addListeners(Stage stage) {
        stage.setOnCloseRequest(e -> {
            if (advBox.getMediaPlayer() != null) {
                advBox.getMediaPlayer().stop();
                advBox.getMediaPlayer().setMute(true);
            }
        });
    }

    private MediaPlayer createPlayer() {
        MediaPlayer player = new MediaPlayer(Objects.requireNonNull(Advertisement.next()));
        player.setAutoPlay(true);
        player.setOnEndOfMedia(() -> advBox.setMediaPlayer(createPlayer()));
        return player;
    }
}
