package com.ardian.taskmanager_spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private NutzerRepository nutzerRepository;

    private Nutzer getEingeloggterNutzer(Authentication authentication) {
        return nutzerRepository.findByUsername(authentication.getName());
    }

    @GetMapping
    public List<Task> alleTasksAnzeigen() {
        return taskRepository.findAll();
    }

    @PostMapping
    public Task taskErstellen(@RequestBody Task task, Authentication authentication) {
        if (task.getStatus() == null || task.getStatus().isEmpty()) {
            throw new IllegalArgumentException("Der Status des Tasks darf nicht leer sein!");
        }
        if (task.getTitel() == null || task.getTitel().isEmpty()) {
            throw new IllegalArgumentException("Der Titel des Tasks darf nicht leer sein!");
        }
        if (task.getProjekt() == null) {
            throw new IllegalArgumentException("Die Task muss einem Projekt zugeordnet sein!");
        }
        Nutzer eingeloggterNutzer = getEingeloggterNutzer(authentication);
        task.setNutzer(eingeloggterNutzer);
        return taskRepository.save(task);
    }

    // Findet ein Task per ID und aktualisiert den Status
    @PutMapping("/{id}/status")
    public Task status(@PathVariable Long id, @RequestBody String neuerStatus) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setStatus(neuerStatus);
        return taskRepository.save(task);
    }

    @DeleteMapping("/{id}")
    public void taskDelete(@PathVariable Long id, Authentication authentication) {
        Task task = taskRepository.findById(id).orElseThrow();
        Nutzer eingeloggterNutzer = getEingeloggterNutzer(authentication);
        if (task.getNutzer() == null || !task.getNutzer().equals(eingeloggterNutzer)) {
            throw new AccessDeniedException("Diese Task gehört Ihnen nicht!");
        }
        taskRepository.delete(task);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    }

}
