package SuperVend.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class Inventory {
    private static final ArrayList<String> categories;
    private static final TreeMap<String, Integer> inventory;

    static {
        inventory = new TreeMap<>();
        Scanner in = new Scanner(ResourceManager.readFile("csv/Inventory.csv"));
        while (in.hasNext()) {
            String[] line = in.nextLine().split(",");
            inventory.put(line[1], Integer.parseInt(line[2]));
        }
        categories = ProductCategories.getCategories();
        in.close();
    }

    public static boolean add(String productID, int quantity) {
        if (!categories.contains(productID.substring(0, 2))) return false;
        inventory.put(productID, quantity);
        writeData();
        return true;
    }

    public static boolean delete(String productID) {
        if (!inventory.containsKey(productID)) return false;
        inventory.remove(productID);
        writeData();
        return true;
    }

    public static boolean updateQuantity(String productID, int quantity) {
        if (!inventory.containsKey(productID)) return false;
        inventory.replace(productID, quantity);
        writeData();
        return true;
    }

    public static int getQuantity(String productID) {
        return inventory.getOrDefault(productID, 0);
    }

    public static void writeData() {
        PrintWriter out = new PrintWriter(ResourceManager.writeFile("csv/Inventory.csv"));
        for (String keys : inventory.keySet()) {
            out.println(keys.substring(0, 2) + ',' + keys + ',' + inventory.get(keys));
        }
        out.close();
    }
}
