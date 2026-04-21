package storage;

import java.util.HashMap;
import java.util.Map;

public class CredentialsStorage {
    private Map<String, String> credentials;

    public CredentialsStorage() {
        this.credentials = new HashMap<>();
    }

    public CredentialsStorage(Map<String, String> credentials) {
        this.credentials = credentials;
    }

    public Map<String, String> getCredentials() {
        return credentials;
    }

    public void setCredentials(Map<String, String> credentials) {
        this.credentials = credentials;
    }
}