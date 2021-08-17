package SuperVend.model;

import java.util.ArrayList;

public class ShoppingCart extends ArrayList<Product> {
    public double sum() {
        double sum = 0;
        for (int i = 0; i < super.size(); i++) {
            sum += super.get(i).getPrice();
        }
        return sum;
    }

    public double returnCash(double payment) {
        return payment - sum();
    }

    public ArrayList<ArrayList<Product>> bagging() {
        ArrayList<Integer> remainingWt = new ArrayList<>();
        ArrayList<Short> temp = new ArrayList<>();
        ArrayList<ArrayList<Product>> bagged = new ArrayList<>();

        for (int i = 0; i < super.size(); i++) {
            Product toPack = super.get(i);
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
