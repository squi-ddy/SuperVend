package SuperVend.controllers;

import SuperVend.Main;
import SuperVend.model.Product;
import SuperVend.model.ShoppingCart;
import javafx.event.ActionEvent;
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
    public void checkoutAction(ActionEvent e) {
        errLabel.setVisible(false);
        try {
            if (Double.parseDouble(payingTF.getText()) < ShoppingCart.sum()) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            errLabel.setText("Enter a valid payment amount!");
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
            CartTileController ct = new CartTileController(p, cartItemsVBox, () -> sumText.setText(String.format("%.2f", ShoppingCart.sum())));
            cartItemsVBox.getChildren().add(ct.constructTile());
        }
        if (cartItemsVBox.getChildren().size() == 0) {
            Label lab = new Label("No items!");
            VBox.setMargin(lab, new Insets(5));
            cartItemsVBox.getChildren().add(lab);
        }
        sumText.setText(String.format("%.2f", ShoppingCart.sum()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
    }
}
