package SuperVend.model;

import javafx.scene.media.Media;

import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Advertisement {
    // Responsible for advert content.
    private static final ArrayList<String> advFileNames;
    private static int currPt;

    static {
        currPt = 0;
        advFileNames = new ArrayList<>();
        Scanner in = new Scanner(ResourceManager.readFile("csv/Advertisement.csv"));
        in.useDelimiter(",");
        while (in.hasNext()) advFileNames.add(in.next());
        in.close();
    }

    public static Media next() {
        if (currPt == advFileNames.size()) currPt = 0;
        Path filePath = ResourceManager.guaranteeExists("ads/" + advFileNames.get(currPt));
        currPt++;
        try {
            URL file = filePath.toUri().toURL();
            return new Media(file.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void delete(String fileName) {
        advFileNames.remove(fileName);
    }

    public static void add(String fileName) {
        advFileNames.add(fileName);
    }

    public static ArrayList<String> getAll() {
        return advFileNames;
    }

    public static void writeData() {
        PrintWriter out = new PrintWriter(ResourceManager.writeFile("csv/Advertisement.csv"));
        String toWrite = String.join(",", advFileNames);
        out.println(toWrite);
        out.close();
    }

    public static void reset() {
        currPt = 0;
    }
}
