package SuperVend.view;

import SuperVend.model.Product;
import SuperVend.model.ProductCategories;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.TreeMap;

public class ProductLister {
    private final Accordion rootNode;
    private final TreeMap<String, ArrayList<Product>> products;
    private final SelectionHandler selectionHandler;

    public ProductLister(Accordion root, TreeMap<String, ArrayList<Product>> products, SelectionHandler selectionHandler) {
        this.rootNode = root;
        this.products = products;
        this.selectionHandler = selectionHandler;
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
            prod.setOnMouseClicked(e -> selectionHandler.handlePress(prod, e, () -> {
                // todo: change main screen
            }));
            res.getChildren().add(prod);
        }
        res.setStyle("-fx-padding: -1;");
        return res;
    }
}
