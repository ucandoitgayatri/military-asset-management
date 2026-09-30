package com.example.militaryassetmanagement.controller;

import com.example.militaryassetmanagement.entity.User;
import com.example.militaryassetmanagement.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public AuthController(
            AuthenticationManager authenticationManager,
            UserRepository userRepository) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.username(),
                                loginRequest.password()
                        )
                );

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(
                context,
                request,
                response
        );

        User user = userRepository.findByUsername(
                authentication.getName()
        ).orElseThrow(() ->
                new RuntimeException("User not found")
        );

        Map<String, Object> result = new HashMap<>();

        result.put("message", "Login successful");
        result.put("username", user.getUsername());
        result.put("role", authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority());

        if (user.getBase() != null) {
            result.put("baseId", user.getBase().getId());
            result.put("baseName", user.getBase().getName());
        } else {
            result.put("baseId", null);
            result.put("baseName", null);
        }

        return ResponseEntity.ok(result);
    }

    public record LoginRequest(
            String username,
            String password
    ) {
    }
}