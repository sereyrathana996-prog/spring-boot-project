package com.example.main;

import org.springframework.stereotype.Service;
// import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    // User data is stored in memory (key = username).
    // private final Map<String, User> userDatabase = new ConcurrentHashMap<>();

    // public UserService() {
    //     // Seed one sample account.
    //     userDatabase.put("admin", new User("admin", "admin@beltei.edu.kh", "123456"));
    // }

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public boolean exists(String username) {
        return username != null
                && userRepository.existsByUsernameIgnoreCase(username.trim());
    }

    public boolean register(String username, String email, String password) {
        if (username == null || username.isBlank()
                || password == null || exists(username)) {
            return false;
        }

        String cleanUsername = username.trim();
        String cleanEmail = email == null ? "" : email.trim();

        userRepository.save(
                new User(cleanUsername, cleanEmail, password)
        );
        return true;
    }

    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        return userRepository.findByUsernameIgnoreCase(username.trim())
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    public User findByUsername(String username) {
        if (username == null) {
            return null;
        }

        return userRepository.findByUsernameIgnoreCase(username.trim())
                .orElse(null);
    }
}
