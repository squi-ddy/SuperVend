package model;

import java.util.ArrayList;
import java.util.Date;

public class Product {
    public static final short SMALL = 0;
    public static final short MEDIUM = 1;
    public static final short LARGE = 2;

    public static final short ROOM_TEMP = 10;
    public static final short CHILL = 11;
    public static final short FREEZE = 12;

    private String productID;
    private String name;
    private String description;
    private String brand;
    private double price;
    private int storageTemp;
    private short size;
    private String origin;
    private Date expiry;
    private double weight;
    private ArrayList<String> images;

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public Date getExpiry() {
        return expiry;
    }

    public int getStorageTemp() {
        return storageTemp;
    }

    public double getWeight() {
        return weight;
    }

    public short getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }

    public String getOrigin() {
        return origin;
    }

    public String getProductID() {
        return productID;
    }

    public short getStorageType() {
        if (storageTemp >= 20) return ROOM_TEMP;
        if (storageTemp >= 0) return CHILL;
        return FREEZE;
    }

    public int getSizeUnits() {
        // Return how many small size items this item is equal to
        if (size == SMALL) return 1;
        if (size == MEDIUM) return 2;
        return 4;
    }
}
