package com.shop.controller;

import com.shop.dto.*;
import com.shop.entity.User;
import com.shop.security.JwtTokenProvider;
import com.shop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        User user = userService.registerUser(registrationDto);
        return ResponseEntity.ok(ApiResponse.success("User Submitted Successfully", user));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        User user = userService.authenticateUser(loginDto);

        String jwt = tokenProvider.generateToken(
            user.getUsername(),
            user.getId(),
            user.getRole().toString()
        );

        JwtResponse jwtResponse = new JwtResponse(
            jwt,
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole().toString()
        );

        return ResponseEntity.ok(ApiResponse.success("User Logged", jwtResponse));
    }
}