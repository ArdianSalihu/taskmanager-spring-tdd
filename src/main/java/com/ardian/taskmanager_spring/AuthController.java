package com.ardian.taskmanager_spring;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private NutzerRepository nutzerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/registrieren")
    public String registrieren(@RequestBody Nutzer nutzer) {
        if (nutzer.getUsername() == null || nutzer.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username darf nicht leer sein!");
        }
        if (nutzer.getPassword() == null || nutzer.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Passwort darf nicht leer sein!");
        }
        if (nutzerRepository.findByUsername(nutzer.getUsername()) != null) {
            throw new IllegalArgumentException("Nutzername ist schon vergeben!");
        }

        nutzer.setPassword(passwordEncoder.encode(nutzer.getPassword()));
        nutzerRepository.save(nutzer);
        return "Registrierung erfolgreich!";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
