package org.example.todoapi.service;

import org.example.todoapi.dto.JwtResponse;
import org.example.todoapi.dto.LoginRequest;
import org.example.todoapi.dto.RegisterRequest;
import org.example.todoapi.entity.User;
import org.example.todoapi.enums.Role;
import org.example.todoapi.repository.UserRepository;
import org.example.todoapi.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepo, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest req) {

        if (userRepo.existsByUsername(req.username())) {
            throw new RuntimeException("Username exists");
        }

        if (userRepo.existsByEmail(req.email())) {
            throw new RuntimeException("Email exists");
        }

        User user = new User();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPassword(encoder.encode(req.password()));
        user.setRole(Role.USER);

        userRepo.save(user);
    }


    public JwtResponse login(LoginRequest req) {
        User user = userRepo.findByUsername(req.username()).orElseThrow(() -> new RuntimeException("Invalid username or password"));
        if (!encoder.matches(req.password(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        String token = jwtUtil.generateToken(user);
        return new JwtResponse(token);
    }
}
