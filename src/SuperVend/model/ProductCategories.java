package SuperVend.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeMap;

public class ProductCategories {
    private static final TreeMap<String, String> categories;
    private static final Path rootFP = Path.of(System.getProperty("user.dir"));

    static {
        categories = new TreeMap<>();
        Path catFilePath = rootFP.resolve(Path.of("csv/ProductCode.csv"));
        File catFile = new File(String.valueOf(catFilePath));
        if (!catFile.exists()) {
            try {
                Files.createDirectories(catFilePath.getParent());
                Files.copy(Objects.requireNonNull(ProductCategories.class.getResourceAsStream("/csv/ProductCode.csv")), catFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Scanner in = new Scanner(catFile);
            while (in.hasNext()) {
                String[] line = in.nextLine().split(",");
                categories.put(line[0], line[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getCategories() {
        return new ArrayList<>(categories.keySet());
    }

    public static String getFullName(String category) {
        return categories.get(category);
    }

    public static void addCategory(String shortName, String fullName) {
        categories.put(shortName, fullName);
    }

    public static void writeData() {
        Path catFilePath = rootFP.resolve(Path.of("csv/ProductCode.csv"));
        File catFile = new File(String.valueOf(catFilePath));
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream(catFile));
            for (String keys : categories.keySet()) {
                out.write(keys + ',' + categories.get(keys));
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
