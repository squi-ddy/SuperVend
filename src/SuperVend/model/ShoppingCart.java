package SuperVend.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingCart {
    private static final ArrayList<Product> cart;
    private static final HashMap<Product, Integer> productCount;

    static {
        cart = new ArrayList<>();
        productCount = new HashMap<>();
    }

    public static void add(Product p, int quantity) {
        cart.add(p);
        productCount.put(p, productCount.getOrDefault(p, 0) + quantity);
    }

    public static void delete(Product p) {
        productCount.remove(p);
        cart.remove(p);
    }

    public static void clearAll() {
        productCount.clear();
        cart.clear();
    }

    public static int getProductCount(Product e) {
        return productCount.getOrDefault(e, 0);
    }

    public static double sum() {
        double sum = 0;
        for (Product product : cart) {
            sum += product.getPrice();
        }
        return sum;
    }

    public static double returnCash(double payment) {
        return payment - sum();
    }

    public static ArrayList<ArrayList<Product>> bagging() {
        ArrayList<Integer> remainingWt = new ArrayList<>();
        ArrayList<Short> temp = new ArrayList<>();
        ArrayList<ArrayList<Product>> bagged = new ArrayList<>();

        for (Product toPack : cart) {
            for (int j = 0; j < remainingWt.size(); j++) {
                int remWt = remainingWt.get(j);
                if (remWt >= toPack.getSizeUnits() && temp.get(j) == toPack.getStorageType()) {
                    bagged.get(j).add(toPack);
                    remainingWt.set(j, remWt - toPack.getSizeUnits());
                    break;
                }
            }
            ArrayList<Product> newBag = new ArrayList<>();
            newBag.add(toPack);
            remainingWt.add(8 - toPack.getSizeUnits());
            temp.add(toPack.getStorageType());
            bagged.add(newBag);
        }

        return bagged;
    }
}
