package SuperVend.controllers;

import javafx.scene.Node;

public class SelectionController {
    private Node selected;

    public void handlePress(Node node, Runnable todo) {
        if (selected != null) selected.getStyleClass().remove("handlerSelected");
        node.getStyleClass().add("handlerSelected");
        selected = node;
        todo.run();
    }

}
