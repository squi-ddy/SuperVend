package SuperVend.view;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class SelectionHandler {
    private Node selected;

    public void handlePress(Node node, MouseEvent e, Runnable todo) {
        if (selected != null) selected.setStyle("");
        node.setStyle("-fx-background-color: palegreen; ");
        selected = node;
        todo.run();
    }

}
