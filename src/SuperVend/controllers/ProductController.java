package SuperVend.controllers;

import SuperVend.model.*;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ProductController {
    @FXML
    private Label nameLabel;
    @FXML
    private Pagination imagePagination;
    @FXML
    private Label stockLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label descLabel;
    @FXML
    private HBox moreInfoHBox;
    @FXML
    private Text sizeText;
    @FXML
    private Text countryText;
    @FXML
    private Text weightText;
    @FXML
    private Text tempText;
    @FXML
    private Text expiryText;
    @FXML
    private Text prodIDText;
    @FXML
    private Text brandText;
    @FXML
    private Label infoLabel;
    @FXML
    private TextField quantityTF;
    @FXML
    private Label errLabel;

    private boolean showingMore;
    private Product product;

    @FXML
    public void cartAction() {
        int toBuy;
        try {
            toBuy = Integer.parseInt(quantityTF.getText());
            if (toBuy < 1) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            errLabel.setText("Enter a valid number!");
            errLabel.setStyle("-fx-text-fill: red; ");
            return;
        }
        if (ShoppingCart.getProductCount(product) + toBuy <= Inventory.getQuantity(product.getProductID())) {
            try {
                ShoppingCart.add(product, toBuy);
            } catch (IllegalArgumentException e) {
                errLabel.setText("Not enough space in cart!");
                errLabel.setStyle("-fx-text-fill: red; ");
                return;
            }
            errLabel.setText("Added " + toBuy + " " + product.getName() + " to the cart!");
            errLabel.setStyle("-fx-text-fill: royalblue; ");
        } else {
            errLabel.setText("Not enough in stock!");
            errLabel.setStyle("-fx-text-fill: red; ");
        }
    }

    public void init(Product p) {
        this.product = p;
        showingMore = false;
        nameLabel.setText(p.getName());
        imagePagination.setPageCount(p.getImages().size());
        imagePagination.setPageFactory(page -> {
            ImageView iv = new ImageView(new Image(ResourceManager.readFile("products/" + p.getImages().get(page))));
            iv.setFitHeight(300);
            iv.setFitWidth(300);
            iv.setPreserveRatio(true);
            return iv;
        });
        stockLabel.setText(Inventory.getQuantity(p.getProductID()) + " in stock");
        infoLabel.setText("See More...");
        priceLabel.setText(String.format("$%.2f", p.getPrice()));
        descLabel.setText(p.getDescription());
        sizeText.setText(Product.sizeToLongString(p.getSize()));
        countryText.setText(p.getOrigin());
        moreInfoHBox.setVisible(false);
        moreInfoHBox.setManaged(false);
        weightText.setText(String.valueOf(p.getWeight()));
        tempText.setText(String.valueOf(p.getStorageTemp()));
        expiryText.setText(ProductManager.formatDate(p.getExpiry()));
        prodIDText.setText(p.getProductID());
        quantityTF.clear();
        brandText.setText(p.getBrand());
        infoLabel.setOnMouseClicked(e -> {
            if (!showingMore) {
                moreInfoHBox.setManaged(true);
                moreInfoHBox.setVisible(true);
                infoLabel.setText("See Less...");
                ScaleTransition st = new ScaleTransition(Duration.millis(300), moreInfoHBox);
                st.setFromY(0);
                st.setToY(1);
                st.play();
            } else {
                infoLabel.setText("See More...");
                ScaleTransition st = new ScaleTransition(Duration.millis(300), moreInfoHBox);
                st.setFromY(1);
                st.setToY(0);
                st.play();
                st.setOnFinished(ae -> {
                    moreInfoHBox.setManaged(false);
                    moreInfoHBox.setVisible(false);
                });
            }
            showingMore = !showingMore;
        });
        errLabel.setText("");
    }
}
