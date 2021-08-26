package SuperVend.controllers;

import SuperVend.model.Move;
import SuperVend.model.ResourceManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FileWindowController {
    @FXML
    private VBox fileListVBox;
    @FXML
    private Label fileTypeLabel;

    private Path baseFilePath;
    private BiConsumer<Path, Stage> onPreview;
    private TreeSet<Path> filePaths;
    private Consumer<ArrayList<Path>> onSubmit;
    private ArrayList<String> validExtensions;
    private String prefix;
    private LinkedList<Move> moves;
    private boolean increment;

    @FXML
    public void submitAction() {
        onSubmit.accept(new ArrayList<>(filePaths));
        doAllTransfers();
        fileTypeLabel.getScene().getWindow().hide();
    }

    @FXML
    public void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Resource Files", validExtensions));
        List<File> selected = fileChooser.showOpenMultipleDialog(fileListVBox.getScene().getWindow());
        if (selected == null) return;
        for (File f : selected) {
            String name = (prefix == null ? f.getName() : (prefix + (increment ? filePaths.size() + 1 : "") + f.getName().substring(f.getName().lastIndexOf('.'))));
            Path newFP = baseFilePath.resolve(Path.of(name));
            moves.add(new Move(f.toPath(), newFP, true));
            filePaths.add(newFP);
        }
        constructTree();
    }

    public void init(Path baseFilePath, ArrayList<String> fileNames, BiConsumer<Path, Stage> onPreview, ArrayList<String> validExtensions, Consumer<ArrayList<Path>> onSubmit, String fileType) {
        init(baseFilePath, fileNames, onPreview, validExtensions, onSubmit, fileType, null);
    }

    public void init(Path baseFilePath, ArrayList<String> fileNames, BiConsumer<Path, Stage> onPreview, ArrayList<String> validExtensions, Consumer<ArrayList<Path>> onSubmit, String fileType, String prefix) {
        init(baseFilePath, fileNames, onPreview, validExtensions, onSubmit, fileType, prefix, true);
    }

    public void init(Path baseFilePath, ArrayList<String> fileNames, BiConsumer<Path, Stage> onPreview, ArrayList<String> validExtensions, Consumer<ArrayList<Path>> onSubmit, String fileType, String prefix, boolean increment) {
        // if prefix is given, we assume it is ordered in nature, with the files being called prefix1.xxx, prefix2.xxx, ...
        moves = new LinkedList<>();
        this.baseFilePath = baseFilePath;
        this.filePaths = new TreeSet<>();
        for (String fileName : fileNames) {
            this.filePaths.add(baseFilePath.resolve(Path.of(fileName)));
            ResourceManager.guaranteeExists(baseFilePath.resolve(Path.of(fileName)));
        }
        this.onPreview = onPreview;
        this.validExtensions = validExtensions;
        this.onSubmit = onSubmit;
        this.prefix = prefix;
        this.increment = increment;
        fileTypeLabel.setText(fileType);
        if (prefix != null) moveFiles();
        constructTree();
    }

    private HBox constructTile(Path filePath) {
        HBox root = new HBox();
        VBox.setMargin(root, new Insets(3,3,0,3));
        root.setAlignment(Pos.CENTER);
        Label nameLabel = new Label(filePath.getFileName().toString());
        HBox.setMargin(nameLabel, new Insets(3));
        root.setStyle("-fx-border-color: black;");
        ImageView x = new ImageView(new Image(ResourceManager.readFile("imgs/xicon.png")));
        x.setFitWidth(10.);
        x.setFitHeight(10.);
        Button removeButton = new Button("");
        removeButton.setGraphic(x);
        HBox.setMargin(removeButton, new Insets(3));
        removeButton.setOnAction(e -> {
            filePaths.remove(filePath);
            moves.add(new Move(filePath, null));
            moveFiles();
        });
        root.setOnMouseClicked(e -> {
            if (e.getClickCount() >= 2 && e.getButton() == MouseButton.PRIMARY) {
                Stage stage = new Stage();
                onPreview.accept(findRealFP(filePath), stage);
                stage.setTitle(filePath.getFileName().toString());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.showAndWait();
            }
        });
        root.getChildren().addAll(nameLabel, removeButton);
        return root;
    }

    private void moveFiles() {
        if (prefix == null || !increment) return;
        int i = 0;
        TreeSet<Path> paths = new TreeSet<>();
        for (Path path : filePaths) {
                i++;
                Path newPath = path.resolveSibling(prefix + i + path.toString().substring(path.toString().lastIndexOf(".")));
                moves.add(new Move(path, newPath));
                paths.add(newPath);
        }
        filePaths.clear();
        filePaths.addAll(paths);
        constructTree();
    }

    private void constructTree() {
        fileListVBox.getChildren().clear();
        for (Path path : filePaths) {
            fileListVBox.getChildren().add(constructTile(path));
        }
    }

    private void doAllTransfers() {
        try {
            while (!moves.isEmpty()) {
                Move next = moves.remove();
                if (next.getDestination() == null) {
                    Files.delete(next.getOrigin());
                } else if (next.isCopy()) {
                    Files.copy(next.getOrigin(), next.getDestination(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    Files.move(next.getOrigin(), next.getDestination());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Path findRealFP(Path dest) {
        // look through the linked list in reverse order, keeping track of which file we are currently on. The last file is our real file.
        Iterator<Move> it = moves.descendingIterator();
        while (it.hasNext()) {
            Move next = it.next();
            if (Objects.equals(next.getDestination(), dest)) {
                dest = next.getOrigin();
            }
        }
        return dest;
    }
}
