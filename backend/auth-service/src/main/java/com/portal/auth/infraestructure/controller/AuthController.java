package com.portal.auth.infraestructure.controller;

import com.portal.auth.application.AuthService;
import com.portal.auth.application.UserService;
import com.portal.auth.domain.model.User;
import com.portal.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String googleClientId;

    public AuthController(UserService userService, AuthService authService, JwtUtil jwtUtil,
                          @org.springframework.beans.factory.annotation.Value("${app.oauth.google.client-id}") String googleClientId) {
        this.userService = userService;
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.googleClientId = googleClientId;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("passwordHash");

        return authService.login(email, password)
                .map(token -> ResponseEntity.ok(Map.of("token", token)))
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }

    @PostMapping("/login/google")
    public ResponseEntity<?> loginGoogle(@RequestBody Map<String, String> body) {
        String idToken = body.get("id_token");
        if (idToken == null) return ResponseEntity.badRequest().body(Map.of("error", "id_token missing"));

        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
        Map<String, String> resp;
        try {
            resp = restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid Google id_token"));
        }

        String aud = resp.get("aud");
        if (!googleClientId.equals(aud)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid Google client id"));
        }

        String email = resp.get("email");
        String googleSub = resp.get("sub");
        String name = resp.get("name");

        Optional<User> maybe = userService.getUserByEmail(email);
        User user;
        if (maybe.isPresent()) {
            user = maybe.get();
        } else {
            User newUser = new User(null, email, name, "GOOGLE", googleSub, null, User.Role.USER);
            user = userService.createUser(newUser);
        }

        String token = jwtUtil.generateToken(String.valueOf(user.getId()), user.getRole().name());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
