package SuperVend.controllers;

import SuperVend.model.Advertisement;
import SuperVend.model.ResourceManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AboutController implements Initializable {
    @FXML
    private MediaView advBox;
    @FXML
    private Text verLabel;
    @FXML
    private ImageView logoImg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Advertisement.reset();
        advBox.setMediaPlayer(createPlayer());
        logoImg.setImage(new Image(ResourceManager.readFile("imgs/logo.png")));
    }

    public void addListeners(Stage stage) {
        stage.setOnCloseRequest(e -> advBox.getMediaPlayer().stop());
    }

    private MediaPlayer createPlayer() {
        MediaPlayer player = new MediaPlayer(Objects.requireNonNull(Advertisement.next()));
        player.setAutoPlay(true);
        player.setOnEndOfMedia(() -> advBox.setMediaPlayer(createPlayer()));
        return player;
    }
}
