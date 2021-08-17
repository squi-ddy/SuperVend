module SuperVend {
    opens SuperVend.model;
    opens SuperVend.view;
    opens SuperVend.controllers;
    opens SuperVend;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
}