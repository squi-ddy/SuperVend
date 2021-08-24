package SuperVend.model;

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.TreeMap;

public class Inventory {
    private static final TreeMap<String, Integer> inventory;

    static {
        inventory = new TreeMap<>();
        Scanner in = new Scanner(ResourceManager.readFile("csv/Inventory.csv"));
        while (in.hasNext()) {
            String[] line = in.nextLine().split(",");
            inventory.put(line[1], Integer.parseInt(line[2]));
        }
        in.close();
    }

    public static void add(String productID, int quantity) {
        inventory.put(productID, quantity);
        writeData();
    }

    public static void delete(String productID) {
        inventory.remove(productID);
        writeData();
    }

    public static void updateQuantity(String productID, int quantity) {
        inventory.replace(productID, quantity);
        writeData();
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
