package SuperVend.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProductManager {
    private static final TreeMap<String, Product> productsByID;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat tFormatter = new SimpleDateFormat("HH:mm:ss");

    static {
        productsByID = new TreeMap<>();
        Scanner in = new Scanner(ResourceManager.readFile("csv/Product.csv"));
        while (in.hasNext()) {
            String[] line = in.nextLine().split(",");
            Product result = genProduct(line);
            productsByID.put(line[0], result);
        }
        in.close();
    }

    private static Product genProduct(String[] line) {
        String productID = line[0];
        String name = line[1];
        String desc = line[2];
        String brand = line[3];
        double price = Double.parseDouble(line[4]);
        int temp = Integer.parseInt(line[5]);
        short size = Product.sizeFromString(line[6]);
        String country = line[7];
        Date expiry;
        try {
            expiry = formatter.parse(line[8]);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        double weight = Double.parseDouble(line[9]);
        ArrayList<String> images = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(line, 10, line.length)));
        return new Product(productID, name, desc, brand, price, temp, size, country, expiry, weight, images);
    }

    public static Product getProductsByID(String productID) {
        return productsByID.get(productID);
    }

    public static TreeMap<String, ArrayList<Product>> getProductsByCategory() {
        TreeMap<String, ArrayList<Product>> productTree = new TreeMap<>();
        for (String key: ProductCategories.getCategories()) {
            productTree.put(key, new ArrayList<>());
        }
        for (String prodID : productsByID.keySet()) {
            productTree.get(prodID.substring(0, 2)).add(productsByID.get(prodID));
        }
        return productTree;
    }

    public static String formatDate(Date date) {
        return formatter.format(date);
    }

    public static String formatTime(Date date) { return tFormatter.format(date);}
}
