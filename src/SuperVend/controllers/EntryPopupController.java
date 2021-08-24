package SuperVend.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class EntryPopupController {
    // creates small popups with a few input areas (pure code)
    private final ArrayList<TextField> promptTFs;
    private final ArrayList<String> toEnter;
    private final Function<ArrayList<String>, String> onSubmit;
    private final VBox root;
    private final Stage stage;
    private final SimpleStringProperty errText;

    public EntryPopupController(String[] toEnter, Function<ArrayList<String>, String> onSubmit, String title) {
        this(new ArrayList<>(List.of(toEnter)), onSubmit, title);
    }

    public EntryPopupController(ArrayList<String> toEnter, Function<ArrayList<String>, String> onSubmit, String title) {
        promptTFs = new ArrayList<>();
        this.toEnter = toEnter;
        this.onSubmit = onSubmit;
        root = new VBox();
        errText = new SimpleStringProperty();
        createPopup();
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setWidth(300);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
    }

    private void createPopup() {
        root.setAlignment(Pos.CENTER);
        root.getStylesheets().add("SuperVend/view/style.css");
        root.setStyle("-fx-background-color: powderblue;");
        root.setPadding(new Insets(50, 0, 0, 0));
        for (int i = 0; i < toEnter.size(); i++) {
            root.getChildren().add(createPrompt(toEnter.get(i), i));
        }
        Button submit = new Button("Submit");
        Label errLabel = new Label();
        errLabel.setTextAlignment(TextAlignment.CENTER);
        errLabel.setStyle("-fx-text-fill: red;");
        errLabel.setPrefHeight(50);
        errLabel.setWrapText(true);
        errLabel.textProperty().bind(errText);
        submit.setOnAction(this::doSubmit);
        VBox.setMargin(submit, new Insets(3));
        VBox.setMargin(errLabel, new Insets(3));
        root.getChildren().addAll(submit, errLabel);
    }

    private TextFlow createPrompt(String field, int idx) {
        TextFlow prompt = new TextFlow();
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.getChildren().add(new Label(field + ": "));
        TextField entry = new TextField();
        promptTFs.add(entry);
        prompt.getChildren().add(entry);
        VBox.setMargin(prompt, new Insets(3));
        return prompt;
    }

    private void doSubmit(ActionEvent e) {
        ArrayList<String> results = new ArrayList<>();
        promptTFs.forEach(tf -> results.add(tf.getText()));
        String result = onSubmit.apply(results);
        if (result == null) stage.hide();
        else errText.set(result);
    }
}
