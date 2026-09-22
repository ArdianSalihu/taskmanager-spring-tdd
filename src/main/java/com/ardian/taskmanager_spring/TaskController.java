package com.ardian.taskmanager_spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Task> alleTasksAnzeigen() {
        return taskRepository.findAll();
    }

    @PostMapping
    public Task taskErstellen(@RequestBody Task task) {
        if (task.getStatus() == null || task.getStatus().isEmpty()) {
            throw new IllegalArgumentException("Der Status des Tasks darf nicht leer sein!");
        }
        if (task.getTitel() == null || task.getTitel().isEmpty()) {
            throw new IllegalArgumentException("Der Titel des Tasks darf nicht leer sein!");
        }
        if (task.getProjekt() == null) {
            throw new IllegalArgumentException("Die Task muss einem Projekt zugeordnet sein!");
        }
        return taskRepository.save(task);
    }

    // Findet eine Task per ID und aktualisiert den Status
    @PutMapping("/{id}/status")
    public Task status(@PathVariable Long id, @RequestBody String neuerStatus) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setStatus(neuerStatus);
        return taskRepository.save(task);
    }

    @DeleteMapping("/{id}")
    public void taskDelete(@PathVariable Long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        taskRepository.delete(task);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
