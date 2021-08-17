package SuperVend.model;

import java.util.ArrayList;
import java.util.TreeMap;

public class Inventory {
    private ArrayList<String> categories;
    private TreeMap<String, Integer> inventory;

    public boolean add(String productID, int quantity) {
        if (!categories.contains(productID.substring(0, 2))) return false;
        inventory.put(productID, quantity);
        return true;
    }

    public boolean delete(String productID) {
        if (!inventory.containsKey(productID)) return false;
        else inventory.remove(productID);
        return true;
    }

    public boolean updateQuantity(String productID, int quantity) {
        if (!inventory.containsKey(productID)) return false;
        else inventory.replace(productID, quantity);
        return true;
    }

    public int getQuantity(String productID) {
        return inventory.getOrDefault(productID, -1);
    }
}
