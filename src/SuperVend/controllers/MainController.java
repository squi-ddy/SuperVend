package SuperVend.controllers;

import SuperVend.Main;
import SuperVend.model.ProductManager;
import SuperVend.model.ResourceManager;
import SuperVend.model.Security;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private ImageView logoImg;
    @FXML
    private Accordion productList;
    @FXML
    private Pane contentPane;
    @FXML
    private AnchorPane cartPane;
    @FXML
    private Label cartLabel;

    @FXML
    public void openAbout() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("view/about.fxml")));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            ((AboutController)loader.getController()).addListeners(stage);
            stage.setScene(scene);
            stage.setTitle("About");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoImg.setImage(new Image(ResourceManager.readFile("imgs/logo.png")));
        SelectionController selectionController = new SelectionController();
        cartLabel.getStyleClass().add("productLabels");
        String fxmlName = Security.isAdmin() ? "view/options.fxml" : "view/cart.fxml";
        if (Security.isAdmin()) {
            cartLabel.setText("More Actions...");
        }
        cartPane.setOnMouseClicked(e -> selectionController.handlePress(cartLabel, () -> {
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(fxmlName)));
                AnchorPane.setBottomAnchor(root, 0.);
                AnchorPane.setRightAnchor(root, 0.);
                AnchorPane.setLeftAnchor(root, 0.);
                AnchorPane.setTopAnchor(root, 0.);
                contentPane.getChildren().clear();
                contentPane.getChildren().add(root);

                TranslateTransition tt = new TranslateTransition(Duration.millis(500), contentPane);
                tt.setFromX(-contentPane.getWidth());
                tt.setToX(0);
                tt.play();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));
        ProductListController.init(productList, ProductManager.getAllProductsByCategory(), selectionController, contentPane);
    }
}