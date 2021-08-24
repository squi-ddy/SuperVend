package SuperVend.controllers;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class SelectionController {
    private Node selected;

    public void handlePress(Node node, MouseEvent e, Runnable todo) {
        if (selected != null) selected.getStyleClass().remove("handlerSelected");
        node.getStyleClass().add("handlerSelected");
        selected = node;
        todo.run();
    }

}
