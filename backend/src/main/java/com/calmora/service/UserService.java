package com.calmora.service;

import com.calmora.DTO.auth.AuthResponseDTO;
import com.calmora.DTO.auth.LoginRequestDTO;
import com.calmora.Security.JwtService;
import com.calmora.model.Enum.Role;
import com.calmora.model.User;
import com.calmora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    /** Register a new user */
    public User register(String username, String email, String password) {
        String normalizedEmail = email.trim().toLowerCase();

        // Check if email already exists
        Optional<User> existing = userRepository.findByEmail(normalizedEmail);
        if (existing.isPresent()) {
            System.out.println("[REGISTER] Email already exists: " + normalizedEmail);
            return null;
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setEmail(normalizedEmail);
        user.setPassword(
                passwordEncoder.encode(password)
        );
        user.setRole(Role.USER);

        User saved = userRepository.save(user);
        System.out.println("[REGISTER] Success — username: " + saved.getUsername() + ", email: " + saved.getEmail());
        return saved;
    }



    /** Find user by email */
    public User findByEmail(String email) {
        String normalizedEmail = email.trim().toLowerCase();
        Optional<User> userOpt = userRepository.findByEmail(normalizedEmail);
        return userOpt.orElse(null);
    }

    public AuthResponseDTO login(LoginRequestDTO request){

        Optional<User> userOpt =
                userRepository.findByEmail(
                        request.getEmail().trim().toLowerCase()
                );

        if(userOpt.isEmpty()){
            return null;
        }


        User user = userOpt.get();

        if(passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            String token =
                    jwtService.generateToken(
                            user.getEmail()
                    );

            return new AuthResponseDTO(
                    token,
                    user.getRole().name()
            );
        }
        return null;
    }
}
