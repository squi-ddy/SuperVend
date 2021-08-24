package SuperVend.controllers;

import SuperVend.Main;
import SuperVend.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;

public class AdminOptionsController implements Initializable {
    @FXML
    private VBox productsVBox;
    @FXML
    private VBox catVBox;

    @FXML
    public void newCatAction() {
        new EntryPopupController(
                new String[]{"Short Name", "Full Name"},
                res -> {
                    String shortName = res[0].toUpperCase();
                    if (!shortName.matches("[A-Z]{2}") || ProductCategories.getCategory(shortName) != null) {
                        return "Invalid short name! (must be 2 letters and cannot already exist)";
                    }
                    ProductCategories.addCategory(new Category(shortName, res[1]));
                    createProducts();
                    createCategories();
                    ProductListController.updateProducts(ProductManager.getAllProductsByCategory());
                    return null;
                },
                "Create new Category",
                null
        );
    }

    @FXML
    public void newProductAction() {
        new EntryPopupController(
                new String[]{"Name", "ID (full 7 chars)"},
                res -> {
                    String id = res[1].toUpperCase();
                    String shortName = id.substring(0, 2);
                    if (!id.matches("[A-Z]{2}[0-9]{5}") || ProductManager.getProductsByID(id) != null || ProductCategories.getCategory(shortName) == null) {
                        return "Invalid ID! (ID should be of form AA00000, AA must be a valid category, and no duplicates are allowed)";
                    }
                    ProductManager.addProduct(new Product(id, res[0], "", "", 0., 24, Product.SMALL, "", new Date(), 0., new ArrayList<>()));
                    createProducts();
                    ProductListController.updateProducts(ProductManager.getAllProductsByCategory());
                    return null;
                },
                "Create new Product",
                null
        );
    }

    @FXML
    public void chooseLogo() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("view/filewindow.fxml")));
            Parent root = loader.load();
            Stage stage = new Stage();
            ArrayList<String> images = new ArrayList<>();
            images.add("logo.png");
            ((FileWindowController) loader.getController()).init(
                    ResourceManager.getPath("imgs"),
                    images,
                    (p, s) -> {
                        ImageView iv = new ImageView(new Image(ResourceManager.readFile(p)));
                        iv.setFitHeight(500);
                        iv.setFitWidth(500);
                        iv.setPreserveRatio(true);
                        s.setScene(new Scene(new Group(iv)));
                    },
                    new ArrayList<>(Arrays.asList("*.png", "*.jpg", "*.jpeg")),
                    a -> productsVBox.getScene().getWindow().hide(),
                    "Image",
                    "logo",
                    false
            );
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Choose Image");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void chooseAds() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("view/filewindow.fxml")));
            Parent root = loader.load();
            Stage stage = new Stage();
            ((FileWindowController) loader.getController()).init(
                    ResourceManager.getPath("ads"),
                    Advertisement.get(),
                    (p, s) -> {
                        MediaPlayer player = new MediaPlayer(new Media(p.toUri().toString()));
                        player.setAutoPlay(true);
                        MediaView mv = new MediaView(player);
                        mv.setFitHeight(300);
                        mv.setFitWidth(500);
                        mv.setPreserveRatio(true);
                        s.setOnCloseRequest(e -> {
                            player.stop();
                            player.setMute(true);
                        });
                        Parent stackPane = new StackPane(mv);
                        stackPane.setStyle("-fx-background-color: black;");
                        s.setScene(new Scene(stackPane));
                    },
                    new ArrayList<>(Arrays.asList("*.mp4", "*.flv", "*.fxm")),
                    a -> {
                        Advertisement.clear();
                        for (Path p : a) {
                            Advertisement.add(p.getFileName().toString());
                        }
                    },
                    "Video"
            );
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Choose Videos");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createProducts();
        createCategories();
    }

    private void createProducts() {
        productsVBox.getChildren().clear();
        TreeSet<Product> products = ProductManager.getProducts();
        ArrayList<Category> categories = new ArrayList<>(ProductCategories.getCategories());
        for (Product product : products) {
            productsVBox.getChildren().add(constructTile(product, categories));
        }
    }

    private void createCategories() {
        catVBox.getChildren().clear();
        for (Category cat : ProductCategories.getCategories()) {
            catVBox.getChildren().add(constructCatTile(cat));
        }
    }

    private HBox constructCatTile(Category cat) {
        HBox root = new HBox();
        VBox.setMargin(root, new Insets(5,5,0,5));
        root.setAlignment(Pos.CENTER);
        Label nameLabel = new Label(cat.getFullName());
        HBox.setMargin(nameLabel, new Insets(3));
        root.setStyle("-fx-border-color: black;");
        ImageView x = new ImageView(new Image(ResourceManager.readFile("imgs/edit.png")));
        x.setFitWidth(15.);
        x.setFitHeight(15.);
        Button editButton = new Button("");
        editButton.setGraphic(x);
        HBox.setMargin(editButton, new Insets(3));
        editButton.setOnAction(e -> editCatAction(cat));

        x = new ImageView(new Image(ResourceManager.readFile("imgs/xicon.png")));
        x.setFitWidth(15.);
        x.setFitHeight(15.);
        Button removeButton = new Button("");
        removeButton.setGraphic(x);
        HBox.setMargin(removeButton, new Insets(3));
        removeButton.setOnAction(e -> {
            productsVBox.getChildren().remove(root);
            ProductCategories.deleteCategory(cat);
            createCategories();
            createProducts();
            ProductListController.updateProducts(ProductManager.getAllProductsByCategory());
        });

        root.getChildren().addAll(nameLabel, editButton, removeButton);
        return root;
    }

    private void editCatAction(Category cat) {
        new EntryPopupController(
                new String[]{"Short Name", "Full Name"},
                res -> {
                    String shortName = res[0].toUpperCase();
                    if (!shortName.matches("[A-Z]{2}") || (ProductCategories.getCategory(shortName) != null && ProductCategories.getCategory(shortName) != cat)) {
                        return "Invalid short name! (must be 2 letters and cannot already exist)";
                    }
                    ProductCategories.renameCategory(cat, shortName, res[1]);
                    createProducts();
                    createCategories();
                    ProductListController.updateProducts(ProductManager.getAllProductsByCategory());
                    return null;
                },
                "Edit Category",
                new String[]{cat.getId(), cat.getFullName()}
        );
    }

    private HBox constructTile(Product product, ArrayList<Category> categories) {
        HBox root = new HBox();
        ArrayList<String> categoryNames = new ArrayList<>();
        categories.forEach(e -> categoryNames.add(e.getFullName()));
        VBox.setMargin(root, new Insets(5,5,0,5));
        root.setAlignment(Pos.CENTER);
        Label nameLabel = new Label(product.getName());
        HBox.setMargin(nameLabel, new Insets(3));
        ComboBox<String> category = new ComboBox<>();
        HBox.setMargin(category, new Insets(3));
        category.getStyleClass().add("productCategories");
        category.setItems(FXCollections.observableList(categoryNames));
        category.getSelectionModel().select(ProductCategories.getCategory(product.getProductID().substring(0, 2)).getFullName());
        category.setOnAction(e -> {
            ProductManager.changeID(product, categories.get(category.getSelectionModel().getSelectedIndex()).getId() + product.getProductID().substring(2));
            ProductListController.updateProducts(ProductManager.getAllProductsByCategory());
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
            ProductListController.updateProducts(ProductManager.getAllProductsByCategory());
        });
        root.getChildren().addAll(nameLabel, category, removeButton);
        return root;
    }
}
