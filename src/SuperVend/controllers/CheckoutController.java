package SuperVend.controllers;

import SuperVend.model.Product;
import SuperVend.model.ShoppingCart;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.Collections;

public class CheckoutController {
    @FXML
    private Text changeText;
    @FXML
    private VBox bagsVBox;

    public void init(double payment) {
        changeText.setText(String.format("%.2f", ShoppingCart.returnCash(payment)));
        genBags();
    }

    private void genBags() {
        ArrayList<ArrayList<Product>> bags = ShoppingCart.bagging();
        for (int i = 0; i < bags.size(); i++) {
            ArrayList<Product> bag = bags.get(i);
            VBox bagProducts = new VBox();
            bagProducts.setAlignment(Pos.CENTER);
            Label bagTitle = new Label("Bag " + (i + 1));
            VBox.setMargin(bagTitle, new Insets(5));
            bagProducts.getChildren().add(bagTitle);
            Collections.sort(bag);
            for (Product p : bag) {
                TextFlow tf = new TextFlow();
                Label prodTitle = new Label(p.getName());
                tf.setPadding(new Insets(2));
                tf.getChildren().add(prodTitle);
                tf.setTextAlignment(TextAlignment.CENTER);
                VBox.setMargin(tf, new Insets(1));
                tf.setStyle("-fx-border-color: black;");
                bagProducts.getChildren().add(tf);
            }
            VBox.setMargin(bagProducts, new Insets(5));
            bagProducts.setStyle("-fx-border-color: black; -fx-background-color: papayawhip;");
            bagsVBox.getChildren().add(bagProducts);
        }
    }
}
