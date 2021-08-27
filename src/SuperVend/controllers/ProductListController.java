package SuperVend.controllers;

import SuperVend.model.Category;
import SuperVend.model.Product;
import SuperVend.model.Security;
import javafx.animation.TranslateTransition;
import javafx.scene.Cursor;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.TreeMap;
import java.util.TreeSet;

public class ProductListController {
    private static Accordion rootNode;
    private static SelectionController selectionController;
    private static Pane contentPane;

    public static void init(Accordion rootNode, TreeMap<Category, TreeSet<Product>> products, SelectionController selectionController, Pane contentPane) {
        ProductListController.rootNode = rootNode;
        ProductListController.selectionController = selectionController;
        ProductListController.contentPane = contentPane;
        createTree(products);
    }

    public static Accordion getRoot() {
        return rootNode;
    }

    public static void updateProducts(TreeMap<Category, TreeSet<Product>> products) {
        createTree(products);
    }

    public static void createTree(TreeMap<Category, TreeSet<Product>> products) {
        rootNode.getPanes().clear();
        for (Category category : products.keySet()) {
            TitledPane cat = new TitledPane();
            cat.setText(category.getFullName() + " (" + products.get(category).size() + ")");
            cat.setContent(createProducts(products.get(category)));
            rootNode.getPanes().add(cat);
        }
    }

    private static VBox createProducts(TreeSet<Product> products) {
        VBox res = new VBox();
        for (Product product : products) {
            AnchorPane prod = new AnchorPane();
            Label prodLabel = new Label(product.getName());
            prodLabel.getStyleClass().add("productLabels");
            AnchorPane.setBottomAnchor(prodLabel, 0.);
            AnchorPane.setTopAnchor(prodLabel, 0.);
            AnchorPane.setLeftAnchor(prodLabel, 0.);
            AnchorPane.setRightAnchor(prodLabel, 0.);
            prod.getChildren().add(prodLabel);
            prod.setCursor(Cursor.HAND);
            prod.setOnMouseClicked(e -> selectionController.handlePress(prodLabel, () -> {
                contentPane.getChildren().clear();
                contentPane.getChildren().add(Security.isAdmin() ? AdminProductLoadController.getRoot(product) : ProductLoadController.getRoot(product));
                TranslateTransition tt = new TranslateTransition(Duration.millis(500), contentPane);
                tt.setFromX(-contentPane.getWidth());
                tt.setToX(0);
                tt.play();
            }));
            res.getChildren().add(prod);
        }
        res.setStyle("-fx-padding: 0 -1 0 -1;");
        return res;
    }
}
