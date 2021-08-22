package SuperVend.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class ResourceManager {
    private static final Path rootFP = Path.of(System.getProperty("user.dir"));

    public static FileInputStream readFile(String fp) {
        Path filePath = rootFP.resolve(Path.of(fp));
        File file = new File(String.valueOf(filePath));
        if (!file.exists()) {
            try {
                Files.createDirectories(filePath.getParent());
                if (ResourceManager.class.getResource("/" + fp) == null) {
                    // no default, simply create blank file
                    Files.createFile(filePath);
                } else {
                    // copy default over
                    Files.copy(Objects.requireNonNull(ResourceManager.class.getResourceAsStream("/" + fp)), filePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return is;
    }

    public static FileOutputStream writeFile(String fp) {
        Path filePath = rootFP.resolve(Path.of(fp));
        File file = new File(String.valueOf(filePath));
        if (!file.exists()) {
            try {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return os;
    }
}
