package SuperVend.controllers;

import SuperVend.model.Product;
import SuperVend.model.ResourceManager;
import SuperVend.model.ShoppingCart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CartTile {
    private final Product product;
    private final VBox parent;
    private final Runnable onDelete;

    public CartTile(Product product, VBox parent, Runnable onDelete) {
        this.product = product;
        this.parent = parent;
        this.onDelete = onDelete;
    }

    public HBox constructTile() {
        HBox root = new HBox();
        VBox.setMargin(root, new Insets(5,5,0,5));
        root.setAlignment(Pos.CENTER);
        Label nameLabel = new Label(product.getName());
        HBox.setMargin(nameLabel, new Insets(3));
        Label qtyLabel = new Label("x" + ShoppingCart.getProductCount(product));
        HBox.setMargin(qtyLabel, new Insets(3));
        root.setStyle("-fx-border-color: black;");
        ImageView x = new ImageView(new Image(ResourceManager.readFile("imgs/xicon.png")));
        x.setFitWidth(15.);
        x.setFitHeight(15.);
        Button removeButton = new Button("");
        removeButton.setGraphic(x);
        HBox.setMargin(removeButton, new Insets(3));
        removeButton.setOnAction(e -> {
            parent.getChildren().remove(root);
            ShoppingCart.delete(product);
            onDelete.run();
        });
        root.getChildren().addAll(nameLabel, qtyLabel, removeButton);
        return root;
    }
}
