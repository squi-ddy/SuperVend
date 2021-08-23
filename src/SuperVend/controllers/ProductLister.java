package SuperVend.controllers;

import SuperVend.model.Product;
import SuperVend.model.ProductCategories;
import javafx.scene.Cursor;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class ProductLister {
    private final Accordion rootNode;
    private final TreeMap<String, ArrayList<Product>> products;
    private final SelectionHandler selectionHandler;
    private final Pane contentPane;

    public ProductLister(Accordion root, TreeMap<String, ArrayList<Product>> products, SelectionHandler selectionHandler, Pane contentPane) {
        this.rootNode = root;
        this.products = products;
        this.selectionHandler = selectionHandler;
        this.contentPane = contentPane;
        createTree(products);
    }

    public void createTree(TreeMap<String, ArrayList<Product>> products) {
        for (String category : products.keySet()) {
            TitledPane cat = new TitledPane();
            cat.setText(ProductCategories.getFullName(category));
            cat.setContent(createProducts(products.get(category)));
            rootNode.getPanes().add(cat);
        }
    }

    private VBox createProducts(ArrayList<Product> products) {
        Collections.sort(products);
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
            prod.setOnMouseClicked(e -> selectionHandler.handlePress(prodLabel, e, () -> {
                contentPane.getChildren().clear();
                contentPane.getChildren().add(ProductLoader.getRoot(product));
            }));
            res.getChildren().add(prod);
        }
        res.setStyle("-fx-padding: 0 -1 0 -1;");
        return res;
    }
}
