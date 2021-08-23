package SuperVend.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
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
        String desc = line[2].replace("\\n", "\n").replace("#&!()@", ","); // deals with newlines and commas in description
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

    public static void addProduct(Product p) {
        productsByID.put(p.getProductID(), p);
        writeData();
    }

    public static void deleteProduct(Product p) {
        productsByID.remove(p.getProductID());
        Inventory.delete(p.getProductID());
        // delete all the photos
        for (String img : p.getImages()) {
            Path imgPath = ResourceManager.getPath("products/" + img);
            try {
                Files.deleteIfExists(imgPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeData();
    }

    public static void changeID(Product product, String newID) {
        if (product.getProductID().equals(newID)) return;
        // Safe changing of product ID
        productsByID.remove(product.getProductID());
        productsByID.put(newID, product);
        Inventory.add(newID, Inventory.getQuantity(product.getProductID()));
        Inventory.delete(product.getProductID());
        // move all the photos
        for (String img : product.getImages()) {
            String oldPath = "products/" + img;
            String newPath = oldPath.replace(product.getProductID(), newID);
            ResourceManager.guaranteeExists(oldPath);
            try {
                Files.move(ResourceManager.getPath(oldPath), ResourceManager.getPath(newPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        product.getImages().replaceAll(s -> s.replace(product.getProductID(), newID));
        product.setProductID(newID);
        writeData();
    }

    public static String formatDate(Date date) {
        return formatter.format(date);
    }

    public static Date stringToDate(String date) throws ParseException {
        return formatter.parse(date);
    }

    public static String formatTime(Date date) { return tFormatter.format(date);}

    public static void writeData() {
        PrintWriter out = new PrintWriter(ResourceManager.writeFile("csv/Product.csv"));
        for (Product product : productsByID.values()) {
            out.println(serialise(product));
        }
        out.close();
    }

    private static String serialise(Product product) {
        String productID = product.getProductID();
        String name = product.getName();
        String desc = product.getDescription().replace("\n", "\\n").replace(",", "#&!()@");
        String brand = product.getBrand();
        double price = product.getPrice();
        int temp = product.getStorageTemp();
        String size = Product.sizeToShortString(product.getSize());
        String country = product.getOrigin();
        String expiry = formatter.format(product.getExpiry());
        double wt = product.getWeight();
        ArrayList<String> images = product.getImages();
        return String.join(",", productID, name, desc, brand, String.valueOf(price), String.valueOf(temp), size, country, expiry, String.valueOf(wt), String.join(",", images));
    }
}
