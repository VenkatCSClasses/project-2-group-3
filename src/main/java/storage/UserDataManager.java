package storage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import trade.*;

public class UserDataManager {
    private static final String USERS_DIR = "data/users";
    private static final double DEFAULT_CASH = 10_000.00;

    private static final Map<String, String> CREDENTIALS = new HashMap<>(Map.of(
            "alice",   "password1",
            "bob",     "password2",
            "charlie", "password3"
    ));

    public static User loadUser(String username) {
        String path = USERS_DIR + "/" + username + ".json";
        try {
            UserData data = JsonFileManager.load(path, UserData.class);
            if (data != null) {
                User user = new User(data.getUsername(), data.getCashBalance());
                Portfolio p = data.getPortfolio();
                TransactionLog tl = data.getTransactionLog();
                user.setPortfolio(p != null ? p : new Portfolio());
                user.setTransactionLog(tl != null ? tl : new TransactionLog());
                return user;
            }
        } catch (Exception ignored) {
            // No saved file yet — start fresh
        }
        return new User(username, DEFAULT_CASH);
    }

    public static void saveUser(User user) {
        try {
            File dir = new File(USERS_DIR);
            if (!dir.exists()) dir.mkdirs();

            String path = USERS_DIR + "/" + user.getUsername() + ".json";
            UserData data = new UserData(
                    user.getUsername(),
                    CREDENTIALS.getOrDefault(user.getUsername(), ""),
                    user.getCashBalance(),
                    user.getPortfolio(),
                    user.getTransactionLog()
            );
            JsonFileManager.save(data, path);
        } catch (Exception e) {
            System.out.println("Warning: Could not save: " + e.getMessage());
        }
    }
}
