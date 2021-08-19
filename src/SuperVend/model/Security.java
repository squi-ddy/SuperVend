package SuperVend.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.TreeMap;

public class Security {
    private static String userID;
    private static final TreeMap<String, String> userdata;
    private static final Path rootFP = Path.of(System.getProperty("user.dir"));

    static {
        userdata = new TreeMap<>();
        Path loginFilePath = rootFP.resolve(Path.of("csv/Login.csv"));
        File loginFile = new File(String.valueOf(loginFilePath));
        if (!loginFile.exists()) {
            try {
                Files.createDirectories(loginFilePath.getParent());
                Files.copy(Security.class.getResourceAsStream("/csv/Login.csv"), loginFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Scanner in = new Scanner(loginFile);
            while (in.hasNext()) {
                String[] line = in.nextLine().split(",");
                userdata.put(line[0], line[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean authenticate(String username, String pass) {
        if (!username.matches("(AD|BA)[0-9]{5}")) return false;
        // Password checks: 1. Has alphabet. 2. Has number. 3. Is at least 6 characters.
        if (!pass.matches(".[a-zA-Z].") || !pass.matches(".[0-9].") || pass.length() < 6) return false;
        if (userdata.get(username).equals(pass)) {
            // success
            userID = username;
            return true;
        }
        return false;
    }

    public static String getUserID() {
        return userID;
    }
}
