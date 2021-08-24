package SuperVend.controllers;

import SuperVend.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TreeSet;

public class AdminOptionsController implements Initializable {
    @FXML
    private VBox productsVBox;

    @FXML
    public void newCatAction() {
        new EntryPopupController(
                new String[]{"Short Name", "Full Name"},
                res -> {
                    String shortName = res.get(0).toUpperCase();
                    if (!shortName.matches("[A-Z]{2}") || ProductCategories.getCategories().contains(shortName)) {
                        return "Invalid short name! (must be 2 letters and cannot already exist)";
                    }
                    ProductCategories.addCategory(shortName, res.get(1));
                    return null;
                },
                "Create new Category"
        );
    }

    @FXML
    public void newProductAction() {

    }

    @FXML
    public void chooseLogo() {

    }

    @FXML
    public void chooseAds() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createProducts();
    }

    private void createProducts() {
        productsVBox.getChildren().clear();
        TreeSet<Product> products = ProductManager.getProducts();
        ArrayList<String> categories = ProductCategories.getCategories();
        categories.replaceAll(ProductCategories::getFullName);
        for (Product product : products) {
            productsVBox.getChildren().add(constructTile(product, categories));
        }
    }

    private HBox constructTile(Product product, ArrayList<String> categories) {
        HBox root = new HBox();
        VBox.setMargin(root, new Insets(5,5,0,5));
        root.setAlignment(Pos.CENTER);
        Label nameLabel = new Label(product.getName());
        HBox.setMargin(nameLabel, new Insets(3));
        ComboBox<String> category = new ComboBox<>();
        HBox.setMargin(category, new Insets(3));
        category.getStyleClass().add("productCategories");
        category.setItems(FXCollections.observableList(categories));
        category.getSelectionModel().select(ProductCategories.getFullName(product.getProductID().substring(0, 2)));
        category.setOnAction(e -> {
            ProductManager.changeID(product, ProductCategories.getCategories().get(category.getSelectionModel().getSelectedIndex()) + product.getProductID().substring(2));
            ProductListController.updateProducts(ProductManager.getProductsByCategory());
            createProducts();
        });
        root.setStyle("-fx-border-color: black;");
        ImageView x = new ImageView(new Image(ResourceManager.readFile("imgs/xicon.png")));
        x.setFitWidth(15.);
        x.setFitHeight(15.);
        Button removeButton = new Button("");
        removeButton.setGraphic(x);
        HBox.setMargin(removeButton, new Insets(3));
        removeButton.setOnAction(e -> {
            productsVBox.getChildren().remove(root);
            ProductManager.deleteProduct(product);
            ProductListController.updateProducts(ProductManager.getProductsByCategory());
        });
        root.getChildren().addAll(nameLabel, category, removeButton);
        return root;
    }
}
