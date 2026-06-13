package com.calmora.controller;

import com.calmora.DTO.auth.*;
import com.calmora.model.User;
import com.calmora.service.UserService;
import com.calmora.Security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        String username = request.getUsername();
        String email = request.getEmail();
        String password = request.getPassword();

        if (username == null || email == null || password == null ||
            username.isBlank() || email.isBlank() || password.isBlank()) {

            return  ResponseEntity.badRequest()
                    .body(
                            new RegisterResponseDTO(
                                    false,
                                    "Email already exists.",
                                    null
                            )
                    );
        }

        User user = userService.register(username, email, password);

       return ResponseEntity.ok(
               new RegisterResponseDTO(
                       true,
                       "Registration successful!",
                       user.getUsername()
               )
       );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDTO request
    ) {

        AuthResponseDTO response =
                userService.login(request);

        if(response == null) {

            return ResponseEntity.status(401).body(
                    Map.of(
                            "success", false,
                            "message", "Invalid email or password."
                    )
            );
        }

        return ResponseEntity.ok(response);
    }


    @PostMapping("/google")
    public ResponseEntity<GoogleAuthResponseDTO> googleLogin(@RequestBody GoogleLoginRequestDTO request) {
        String username = request.getUsername();
        String email = request.getEmail();

        if (username == null || email == null || username.isBlank() || email.isBlank()) {
            return ResponseEntity.badRequest().body(
                    new GoogleAuthResponseDTO(
                            false,"email already exists",null,null,null
                    )
            );
        }

        // Check if user already exists
        User existingUser = userService.findByEmail(email);

        if (existingUser != null) {
             String token = jwtService.generateToken(existingUser.getEmail());
             return ResponseEntity.ok(
                      new GoogleAuthResponseDTO(
                              true,"Login Successful",
                              existingUser.getUsername(), existingUser.getEmail(), token
                      )
             );
        }

        // New Google user — auto-register
        User newUser = userService.register(username, email, "GOOGLE_AUTH_USER");

        if (newUser == null) {

            return ResponseEntity.badRequest().body(
                    new GoogleAuthResponseDTO(
                            false,
                            "Registration failed.",
                            null,
                            null,
                            null
                    )
            );
        }

        String token = jwtService.generateToken(newUser.getEmail());
        return ResponseEntity.ok(
                new GoogleAuthResponseDTO(
                        true,
                        "Login successful!",
                        newUser.getUsername(),
                        newUser.getEmail(),
                        token
                )
        );
    }
}
