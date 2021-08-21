package SuperVend.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProductManager {
    //private static productTree;
    private static final TreeMap<String, Product> productsByID;
    private static final Path rootFP = Path.of(System.getProperty("user.dir"));
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    static {
        productsByID = new TreeMap<>();
        Path prodFilePath = rootFP.resolve(Path.of("csv/Product.csv"));
        File prodFile = new File(String.valueOf(prodFilePath));
        if (!prodFile.exists()) {
            try {
                Files.createDirectories(prodFilePath.getParent());
                Files.copy(Objects.requireNonNull(ProductManager.class.getResourceAsStream("/csv/Product.csv")), prodFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Scanner in = new Scanner(prodFile);
            while (in.hasNext()) {
                String[] line = in.nextLine().split(",");
                Product result = genProduct(line);
                productsByID.put(line[0], result);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
        ArrayList<String> images = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(line, 10, line.length - 1)));
        return new Product(productID, name, desc, brand, price, temp, size, country, expiry, weight, images);
    }

    public static TreeMap<String, Product> getProductsByID() {
        return productsByID;
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
}
