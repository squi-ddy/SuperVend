package SuperVend.model;

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class ProductCategories {
    private static final TreeMap<String, Category> categories;

    static {
        categories = new TreeMap<>();
        Scanner in = new Scanner(ResourceManager.readFile("csv/ProductCode.csv"));
        while (in.hasNext()) {
            String[] line = in.nextLine().split(",");
            categories.put(line[0], new Category(line[0], line[1]));
        }
        in.close();
    }

    public static TreeSet<Category> getCategories() {
        return new TreeSet<>(categories.values());
    }

    public static Category getCategory(String id) {
        return categories.get(id);
    }

    public static void addCategory(Category category) {
        categories.put(category.getId(), category);
        writeData();
    }

    public static void deleteCategory(Category category) {
        for (Product product : ProductManager.getProductsByCategory(category)) {
            ProductManager.deleteProduct(product);
        }
        categories.remove(category.getId());
        writeData();
    }

    public static void renameCategory(Category toRename, String newId, String newLongName) {
        for (Product product : ProductManager.getProductsByCategory(toRename)) {
            ProductManager.changeID(product, product.getProductID().replace(toRename.getId(), newId));
        }
        categories.remove(toRename.getId());
        categories.put(newId, toRename);
        toRename.setId(newId);
        toRename.setFullName(newLongName);
        writeData();
    }

    public static void writeData() {
        PrintWriter out = new PrintWriter(ResourceManager.writeFile("csv/ProductCode.csv"));
        for (String keys : categories.keySet()) {
            out.println(keys + ',' + categories.get(keys).getFullName());
        }
        out.close();
    }
}
