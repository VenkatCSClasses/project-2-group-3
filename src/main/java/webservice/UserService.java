package webservice;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import storage.JsonFileManager;
import storage.UserData;
import trade.Portfolio;
import trade.TransactionLog;
import trade.User;
import trade.UserAccount;

@Service
public class UserService {

    private static final String USERS_DIR = "data/users";
    private static final double DEFAULT_CASH = 10_000.00;

    private final Map<String, UserAccount> users = new HashMap<>();
    private final Map<String, User> userCache = new ConcurrentHashMap<>();

    public UserService() {
        users.put("alice",   new UserAccount("alice",   "password1"));
        users.put("bob",     new UserAccount("bob",     "password2"));
        users.put("charlie", new UserAccount("charlie", "password3"));
        loadUsersFromDisk();
    }

    public boolean login(String username, String password) {
        UserAccount account = users.get(username);
        return account != null && account.getPassword().equals(password);
    }

    /** Returns null on success, or an error message on failure. */
    public String register(String username, String password) {
        if (username == null || !username.matches("[a-zA-Z0-9_]{3,20}")) {
            return "Username must be 3–20 characters (letters, numbers, underscores only).";
        }
        if (password == null || password.length() < 6) {
            return "Password must be at least 6 characters.";
        }
        String lower = username.toLowerCase();
        if (users.containsKey(lower)) {
            return "Username is already taken.";
        }
        users.put(lower, new UserAccount(lower, password));
        User newUser = new User(lower, DEFAULT_CASH);
        newUser.setPortfolio(new Portfolio());
        saveUser(newUser);
        return null;
    }

    private void loadUsersFromDisk() {
        File dir = new File(USERS_DIR);
        if (!dir.exists()) return;
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (files == null) return;
        for (File f : files) {
            try {
                UserData data = JsonFileManager.load(f.getPath(), UserData.class);
                if (data != null && data.getUsername() != null && data.getPassword() != null) {
                    String uname = data.getUsername().toLowerCase();
                    users.putIfAbsent(uname, new UserAccount(uname, data.getPassword()));
                }
            } catch (Exception ignored) {}
        }
    }

    public User getUser(String username) {
        return userCache.computeIfAbsent(username, this::loadUser);
    }

    public void saveUser(User user) {
        try {
            File dir = new File(USERS_DIR);
            if (!dir.exists()) dir.mkdirs();
            String path = USERS_DIR + "/" + user.getUsername() + ".json";
            UserAccount account = users.get(user.getUsername());
            String password = account != null ? account.getPassword() : "";
            UserData data = new UserData(user.getUsername(), password,
                    user.getCashBalance(), user.getPortfolio(), user.getTransactionLog());
            JsonFileManager.save(data, path);
            userCache.put(user.getUsername(), user);
        } catch (Exception e) {
            System.err.println("Warning: could not save user: " + e.getMessage());
        }
    }

    private User loadUser(String username) {
        try {
            String path = USERS_DIR + "/" + username + ".json";
            UserData data = JsonFileManager.load(path, UserData.class);
            if (data != null) {
                User user = new User(data.getUsername(), data.getCashBalance());
                Portfolio p = data.getPortfolio();
                user.setPortfolio(p != null ? p : new Portfolio());
                return user;
            }
        } catch (Exception ignored) {}
        return new User(username, DEFAULT_CASH);
    }
}
