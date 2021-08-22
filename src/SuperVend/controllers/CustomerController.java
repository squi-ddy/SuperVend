package SuperVend.controllers;

import SuperVend.Main;
import SuperVend.model.ProductManager;
import SuperVend.model.ResourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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

    @FXML
    public void openAbout(ActionEvent e) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoImg.setImage(new Image(ResourceManager.readFile("imgs/logo.png")));
        SelectionHandler selectionHandler = new SelectionHandler();
        cartPane.getChildren().get(0).getStyleClass().add("productLabels");
        cartPane.setOnMouseClicked(e -> selectionHandler.handlePress(cartPane, e, () -> {
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("view/cart.fxml")));
                AnchorPane.setBottomAnchor(root, 0.);
                AnchorPane.setRightAnchor(root, 0.);
                AnchorPane.setLeftAnchor(root, 0.);
                AnchorPane.setTopAnchor(root, 0.);
                contentPane.getChildren().clear();
                contentPane.getChildren().add(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));
        new ProductLister(productList, ProductManager.getProductsByCategory(), selectionHandler, contentPane);
    }
}