package SuperVend.controllers;

import SuperVend.Main;
import SuperVend.model.Product;
import SuperVend.model.ShoppingCart;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CartController implements Initializable {
    @FXML
    private VBox cartItemsVBox;
    @FXML
    private Text sumText;
    @FXML
    private TextField payingTF;
    @FXML
    private Label errLabel;
    @FXML
    private Text noCartItems;
    @FXML
    private Text maxCartItems;

    @FXML
    public void checkoutAction() {
        errLabel.setVisible(false);
        try {
            if (Double.parseDouble(payingTF.getText()) < ShoppingCart.sum()) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            errLabel.setText("Enter a valid payment amount!");
            errLabel.setVisible(true);
            return;
        }
        if (ShoppingCart.noProducts() == 0) {
            errLabel.setText("Nothing in cart!");
            errLabel.setVisible(true);
            return;
        }
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/checkout.fxml"));
        try {
            Parent root = loader.load();
            ((CheckoutController)loader.getController()).init(Double.parseDouble(payingTF.getText()));
            ShoppingCart.doCheckout();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("SuperVend Checkout");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            init();
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void init() {
        errLabel.setVisible(false);
        cartItemsVBox.getChildren().clear();
        payingTF.clear();
        for (Product p : ShoppingCart.getProducts()) {
            CartTileController ct = new CartTileController(p, cartItemsVBox, this::init);
            cartItemsVBox.getChildren().add(ct.constructTile());
        }
        if (cartItemsVBox.getChildren().size() == 0) {
            Label lab = new Label("No items!");
            VBox.setMargin(lab, new Insets(5));
            cartItemsVBox.getChildren().add(lab);
        }
        noCartItems.setText(String.valueOf(ShoppingCart.noProducts()));
        sumText.setText(String.format("%.2f", ShoppingCart.sum()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        maxCartItems.setText(String.valueOf(Main.cartLimit));
        init();
    }
}
