package SuperVend.controllers;

import SuperVend.model.ProductManager;
import SuperVend.view.ProductLister;
import SuperVend.view.SelectionHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    @FXML
    private ImageView logoImg;
    @FXML
    private Accordion productList;
    @FXML
    private Pane contentPane;
    @FXML
    private AnchorPane cartPane;

    private final Path rootFP = Path.of(System.getProperty("user.dir"));

    @FXML
    public void openAbout(ActionEvent e) {

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
        SelectionHandler selectionHandler = new SelectionHandler();
        cartPane.getChildren().get(0).getStyleClass().add("productLabels");
        cartPane.setOnMouseClicked(e -> selectionHandler.handlePress(cartPane, e, () -> {
            // todo: change main screen
        }));
        new ProductLister(productList, ProductManager.getProductsByCategory(), selectionHandler);
    }
}