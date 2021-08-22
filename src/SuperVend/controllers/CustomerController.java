package SuperVend.controllers;

import SuperVend.model.ProductManager;
import SuperVend.model.ResourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
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
            // todo: change main screen
        }));
        new ProductLister(productList, ProductManager.getProductsByCategory(), selectionHandler, contentPane);
    }
}