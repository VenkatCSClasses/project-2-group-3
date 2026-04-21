package webservice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import trade.UserAccount;

@Service
public class UserService {

    private final Map<String, UserAccount> users = new HashMap<>();

    public UserService() {
        // Pre-populated users
        users.put("alice", new UserAccount("alice", "password1"));
        users.put("bob", new UserAccount("bob", "password2"));
        users.put("charlie", new UserAccount("charlie", "password3"));
    }

    public boolean login(String username, String password) {
        UserAccount account = users.get(username);
        return account != null && account.getPassword().equals(password);
    }
}
