package trade;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request, HttpSession session) {
        if (userService.login(request.getUsername(), request.getPassword())) {
            session.setAttribute("username", request.getUsername());
            return ResponseEntity.ok("Login successful.");
        }
        return ResponseEntity.status(401).body("Invalid username or password.");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request, HttpSession session) {
        String username = request.getUsername();
        String password = request.getPassword();
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body("Username and password are required.");
        }
        String error = userService.register(username.trim().toLowerCase(), password);
        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        session.setAttribute("username", username.trim().toLowerCase());
        return ResponseEntity.ok("Account created.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out.");
    }
}
