package com.ardian.taskmanager_spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(NutzerRepository nutzerRepository) {
        return username -> {
            Nutzer nutzer = nutzerRepository.findByUsername(username);
            if (nutzer == null) {
                throw new UsernameNotFoundException("Nutzer nicht gefunden!");
            }
            return User.builder()
                    .username(nutzer.getUsername())
                    .password(nutzer.getPassword())
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF-Schutz ist für Browser-Formulare gedacht, bei einer REST-API würde er POST/PUT/DELETE blockieren
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/registrieren").permitAll() // Diese eine Adresse darf jeder aufrufen, ohne Login
                        .anyRequest().authenticated() // Jede Anfrage muss authentifiziert sein
                )
                .httpBasic(Customizer.withDefaults()); // Nutzt einfache Basic-Auth (Username/Passwort-Login)


        return http.build();
    }
}
