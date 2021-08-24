package SuperVend.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.TreeMap;

public class ShoppingCart {
    private static final TreeMap<Product, Integer> productCount;

    static {
        productCount = new TreeMap<>();
    }

    public static void add(Product p, int quantity) {
        productCount.put(p, productCount.getOrDefault(p, 0) + quantity);
    }

    public static void delete(Product p) {
        productCount.remove(p);
    }

    public static void clearAll() {
        productCount.clear();
    }

    public static ArrayList<Product> getProducts() {
        return new ArrayList<>(productCount.keySet());
    }

    public static int getProductCount(Product e) {
        return productCount.getOrDefault(e, 0);
    }

    public static double sum() {
        double sum = 0;
        for (Product product : productCount.keySet()) {
            sum += product.getPrice() * productCount.get(product);
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

        for (Product toPack : productCount.keySet()) {
            for (int i = 0; i < productCount.get(toPack); i++) {
                boolean packed = false;
                for (int j = 0; j < remainingWt.size(); j++) {
                    int remWt = remainingWt.get(j);
                    if (remWt >= toPack.getSizeUnits() && temp.get(j) == toPack.getStorageType()) {
                        bagged.get(j).add(toPack);
                        remainingWt.set(j, remWt - toPack.getSizeUnits());
                        packed = true;
                        break;
                    }
                }
                if (!packed) {
                    ArrayList<Product> newBag = new ArrayList<>();
                    newBag.add(toPack);
                    remainingWt.add(8 - toPack.getSizeUnits());
                    temp.add(toPack.getStorageType());
                    bagged.add(newBag);
                }
            }
        }

        return bagged;
    }

    public static void doCheckout() {
        Scanner in = new Scanner(ResourceManager.readFile("csv/Transaction.csv"));
        in.useDelimiter("\\Z");
        String old = in.next();
        in.close();
        PrintWriter out = new PrintWriter(ResourceManager.writeFile("csv/Transaction.csv"));
        out.println(old);
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < 5; i++) name.append((char)((int) (Math.random() * ('Z' - 'A' + 1)) + 'A'));
        String randomName = name.toString();
        for (Product p : productCount.keySet()) {
            Inventory.updateQuantity(p.getProductID(), Inventory.getQuantity(p.getProductID()) - productCount.get(p));
            out.println(randomName + "," + ProductManager.formatDate(new Date()) + "," + ProductManager.formatTime(new Date()) + "," + p.getProductID() + "," + String.format("%.2f", p.getPrice()) + "," + productCount.get(p));
        }
        out.close();
        Inventory.writeData();
        clearAll();
    }
}
