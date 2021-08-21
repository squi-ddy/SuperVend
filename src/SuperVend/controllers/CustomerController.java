package SuperVend.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    @FXML
    private ImageView logoImg;
    @FXML
    private Accordion productList;
    @FXML
    private Pane contentPane;

    @FXML
    public void openAbout(ActionEvent e) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}