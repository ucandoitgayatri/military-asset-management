package com.example.militaryassetmanagement.config;

import com.example.militaryassetmanagement.entity.User;
import com.example.militaryassetmanagement.repository.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // ---------------- SECURITY FILTER ----------------

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                // Disable CSRF for REST API
                .csrf(csrf -> csrf.disable())

                // Enable CORS for React frontend
                .cors(cors -> {})

                .authorizeHttpRequests(auth -> auth

                        // Login is public
                        .requestMatchers("/api/auth/**")
                        .permitAll()

                        // Dashboard
                        .requestMatchers("/api/dashboard/**")
                        .hasAnyRole(
                                "ADMIN",
                                "BASE_COMMANDER"
                        )

                        // Purchases
                        .requestMatchers(
                                "/api/assetManagement/addPurchase",
                                "/api/assetManagement/getAllPurchases"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "BASE_COMMANDER",
                                "LOGISTICS_OFFICER"
                        )

                        // Transfers
                        .requestMatchers(
                                "/api/assetManagement/addTransfer",
                                "/api/assetManagement/getAllTransfers"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "BASE_COMMANDER",
                                "LOGISTICS_OFFICER"
                        )

                        // Assignments
                        .requestMatchers(
                                "/api/assetManagement/addAssignment",
                                "/api/assetManagement/getAllAssignments"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "BASE_COMMANDER"
                        )

                        // Expenditures
                        .requestMatchers(
                                "/api/assetManagement/addExpenditure",
                                "/api/assetManagement/getAllExpenditures"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "BASE_COMMANDER"
                        )

                        // All other requests need authentication
                        .anyRequest()
                        .authenticated()
                );

        return http.build();
    }


    // ---------------- USER DETAILS ----------------

    @Bean
    public UserDetailsService userDetailsService() {

        return username -> {

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() ->
                            new UsernameNotFoundException(
                                    "User not found"
                            )
                    );

            String role = user.getRole();

            // If database contains ROLE_ADMIN,
            // convert it to ADMIN before using .roles()
            if (role.startsWith("ROLE_")) {
                role = role.substring(5);
            }

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(role)
                    .build();
        };
    }


    // ---------------- PASSWORD ----------------

    @Bean
    public PasswordEncoder passwordEncoder() {

        return NoOpPasswordEncoder.getInstance();
    }


    // ---------------- AUTHENTICATION MANAGER ----------------

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return new org.springframework.security.authentication.ProviderManager(
                provider
        );
    }


    // ---------------- CORS ----------------

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        // React frontend
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://military-asset-management-indol.vercel.app"
        ));

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        // Required because React uses credentials: "include"
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}