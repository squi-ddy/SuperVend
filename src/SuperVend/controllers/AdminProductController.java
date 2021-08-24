package SuperVend.controllers;

import SuperVend.Main;
import SuperVend.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class AdminProductController {
    @FXML
    private TextField nameTF;
    @FXML
    private TextField stockTF;
    @FXML
    private TextField priceTF;
    @FXML
    private TextArea descTextArea;
    @FXML
    private TextField sizeTF;
    @FXML
    private TextField originTF;
    @FXML
    private TextField weightTF;
    @FXML
    private TextField tempTF;
    @FXML
    private TextField expiryTF;
    @FXML
    private TextField prodIDTF;
    @FXML
    private Text prodCatText;
    @FXML
    private TextField brandTF;
    @FXML
    private Label errLabel;

    private Product product;

    @FXML
    public void applyAction(ActionEvent e) {
        product.setName(nameTF.getText());
        try {
            int stock = Integer.parseInt(stockTF.getText());
            if (stock < 0) throw new NumberFormatException();
            Inventory.updateQuantity(product.getProductID(), stock);
        } catch (NumberFormatException exc) {
            errLabel.setText("Invalid stock value!");
            stockTF.setText(String.valueOf(Inventory.getQuantity(product.getProductID())));
            return;
        }
        try {
            double price = Double.parseDouble(priceTF.getText());
            if (price < 0) throw new NumberFormatException();
            product.setPrice(price);
        } catch (NumberFormatException exc) {
            errLabel.setText("Invalid price!");
            priceTF.setText(String.format("%.2f", product.getPrice()));
            return;
        }
        descTextArea.setText(descTextArea.getText().replace("\\n", "\n"));
        product.setDescription(descTextArea.getText());
        String size = sizeTF.getText();
        if (size.equals("S") || size.equals("M") || size.equals("L")) {
            product.setSize(Product.sizeFromString(size));
        } else {
            errLabel.setText("Invalid size! (S/M/L only)");
            sizeTF.setText(Product.sizeToShortString(product.getSize()));
            return;
        }
        product.setOrigin(originTF.getText());
        try {
            double weight = Double.parseDouble(weightTF.getText());
            if (weight < 0) throw new NumberFormatException();
            product.setWeight(weight);
        } catch (NumberFormatException exc) {
            errLabel.setText("Invalid weight!");
            weightTF.setText(String.valueOf(product.getWeight()));
            return;
        }
        try {
            int temp = Integer.parseInt(tempTF.getText());
            product.setStorageTemp(temp);
        } catch (NumberFormatException exc) {
            errLabel.setText("Invalid storage temperature!");
            tempTF.setText(String.valueOf(product.getStorageTemp()));
            return;
        }
        try {
            Date expiry = ProductManager.stringToDate(expiryTF.getText());
            product.setExpiry(expiry);
        } catch (ParseException exc) {
            errLabel.setText("Invalid date! (Should be in form DD-MM-YYYY)");
            expiryTF.setText(ProductManager.formatDate(product.getExpiry()));
            return;
        }
        String productID = product.getProductID().substring(0,2) + prodIDTF.getText();
        Product corrProduct = ProductManager.getProductsByID(productID);
        if ((corrProduct != null && corrProduct != product) || !prodIDTF.getText().matches("[0-9]{5}")) {
            errLabel.setText("Invalid product ID! (Must be 5 digits with no overlap)");
            prodIDTF.setText(product.getProductID().substring(2));
            return;
        }
        ProductManager.changeID(product, productID);
        product.setBrand(brandTF.getText());
        ProductManager.writeData();
        Inventory.writeData();
        errLabel.setStyle("-fx-text-fill: royalblue;");
        errLabel.setText("Update successful!");
    }

    @FXML
    public void chooseImageAction(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("view/filewindow.fxml")));
            Parent root = loader.load();
            Stage stage = new Stage();
            ((FileWindowController) loader.getController()).init(
                    ResourceManager.getPath("products"),
                    product.getImages(),
                    (Path p) -> {
                        ImageView iv = new ImageView(new Image(ResourceManager.readFile(p)));
                        iv.setFitHeight(500.);
                        iv.setFitWidth(500.);
                        return new AnchorPane(iv);
                    },
                    new ArrayList<>(Arrays.asList("*.png", "*.jpg", "*.jpeg")),
                    (ArrayList<Path> res) -> {
                        product.getImages().clear();
                        for (Path p : res) {
                            product.getImages().add(p.getFileName().toString());
                        }
                    },
                    "Image",
                    product.getProductID() + "-"
            );
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Choose Image");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            ProductManager.writeData();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void init(Product p) {
        this.product = p;
        nameTF.setText(p.getName());
        stockTF.setText(String.valueOf(Inventory.getQuantity(p.getProductID())));
        priceTF.setText(String.format("%.2f", p.getPrice()));
        descTextArea.setText(p.getDescription());
        sizeTF.setText(Product.sizeToShortString(p.getSize()));
        originTF.setText(p.getOrigin());
        weightTF.setText(String.valueOf(p.getWeight()));
        tempTF.setText(String.valueOf(p.getStorageTemp()));
        expiryTF.setText(ProductManager.formatDate(p.getExpiry()));
        prodIDTF.setText(p.getProductID().substring(2));
        brandTF.setText(p.getBrand());
        prodCatText.setText(p.getProductID().substring(0, 2));
        errLabel.setText("");
    }
}
