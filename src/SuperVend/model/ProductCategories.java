package SuperVend.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ProductCategories {
    private static final TreeMap<String, String> categories;

    static {
        categories = new TreeMap<>();
        Scanner in = new Scanner(ResourceManager.readFile("csv/ProductCode.csv"));
        while (in.hasNext()) {
            String[] line = in.nextLine().split(",");
            categories.put(line[0], line[1]);
        }
        in.close();
    }

    public static ArrayList<String> getCategories() {
        return new ArrayList<>(categories.keySet());
    }

    public static String getFullName(String category) {
        return categories.get(category);
    }

    public static void addCategory(String shortName, String fullName) {
        categories.put(shortName, fullName);
        writeData();
    }

    public static void deleteCategory(String name) {
        categories.remove(name);
        writeData();
    }

    public static void writeData() {
        PrintWriter out = new PrintWriter(ResourceManager.writeFile("csv/ProductCode.csv"));
        for (String keys : categories.keySet()) {
            out.write(keys + ',' + categories.get(keys));
        }
        out.close();
    }
}
