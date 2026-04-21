package storage;

import java.util.Map;

public class CredentialsDataManager {
    private static final String CREDS_FILE = "data/credentials/credentials.json";

    public static Map<String, String> loadCredentials() throws Exception {
        CredentialsStorage storage = JsonFileManager.load(CREDS_FILE, CredentialsStorage.class);
        return storage.getCredentials();
    }

    public static void saveCredentials(Map<String, String> credentials) throws Exception {
        CredentialsStorage storage = new CredentialsStorage(credentials);
        JsonFileManager.save(storage, CREDS_FILE);
    }

    public static boolean userExists(String username) throws Exception {
        Map<String, String> credentials = loadCredentials();
        return credentials.containsKey(username.toLowerCase());
    }

    public static boolean validateLogin(String username, String password) throws Exception {
        Map<String, String> credentials = loadCredentials();
        String savedPassword = credentials.get(username.toLowerCase());
        return savedPassword.equals(password);
    }

    public static boolean createAccount(String username, String password) throws Exception {
        Map<String, String> credentials = loadCredentials();
        String cleanUsername = username.toLowerCase().trim();

        if (credentials.containsKey(cleanUsername)) {
            return false;
        }

        credentials.put(cleanUsername, password);
        saveCredentials(credentials);
        return true;
    }
}