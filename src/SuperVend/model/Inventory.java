package SuperVend.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeMap;

public class Inventory {
    private static final ArrayList<String> categories;
    private static final TreeMap<String, Integer> inventory;
    private static final Path rootFP = Path.of(System.getProperty("user.dir"));

    static {
        inventory = new TreeMap<>();
        Path invFilePath = rootFP.resolve(Path.of("csv/Inventory.csv"));
        File invFile = new File(String.valueOf(invFilePath));
        if (!invFile.exists()) {
            try {
                Files.createDirectories(invFilePath.getParent());
                Files.copy(Objects.requireNonNull(Inventory.class.getResourceAsStream("/csv/Inventory.csv")), invFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Scanner in = new Scanner(invFile);
            while (in.hasNext()) {
                String[] line = in.nextLine().split(",");
                inventory.put(line[1], Integer.parseInt(line[2]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        categories = ProductCategories.getCategories();
    }

    public static boolean add(String productID, int quantity) {
        if (!categories.contains(productID.substring(0, 2))) return false;
        inventory.put(productID, quantity);
        return true;
    }

    public static boolean delete(String productID) {
        if (!inventory.containsKey(productID)) return false;
        else inventory.remove(productID);
        return true;
    }

    public static boolean updateQuantity(String productID, int quantity) {
        if (!inventory.containsKey(productID)) return false;
        else inventory.replace(productID, quantity);
        return true;
    }

    public static int getQuantity(String productID) {
        return inventory.getOrDefault(productID, 0);
    }

    public static void writeData() {
        Path invFilePath = rootFP.resolve(Path.of("csv/Inventory.csv"));
        File invFile = new File(String.valueOf(invFilePath));
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream(invFile));
            for (String keys : inventory.keySet()) {
                out.write(keys.substring(0, 2) + ',' + keys + ',' + inventory.get(keys));
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
