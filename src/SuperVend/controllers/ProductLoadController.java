package SuperVend.controllers;

import SuperVend.Main;
import SuperVend.model.Product;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class ProductLoadController {
    private static Parent root;
    private static final ProductController controller;

    // This class caches the product screen so slow fxml loading only occurs once
    static {
        FXMLLoader fxml = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("view/product.fxml")));
        try {
            root = fxml.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = fxml.getController();
    }

    public static Parent getRoot(Product p) {
        controller.init(p);
        AnchorPane.setRightAnchor(root, 0.);
        AnchorPane.setLeftAnchor(root, 0.);
        AnchorPane.setTopAnchor(root, 0.);
        AnchorPane.setBottomAnchor(root, 0.);
        return root;
    }
}
