package com.ardian.taskmanager_spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/projekt")
public class ProjektController {

    @Autowired
    private ProjektRepository projektRepository;

    @GetMapping
    public List<Projekt> alleProjekteAnzeigen() {
        return projektRepository.findAll();
    }

    @PostMapping
    public Projekt projektErstellen(@RequestBody Projekt projekt) {
        if (projekt.getName() == null || projekt.getName().isEmpty()) {
            throw new IllegalArgumentException("Der Name des Projekts darf nicht leer sein!");
        }
        return projektRepository.save(projekt);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
