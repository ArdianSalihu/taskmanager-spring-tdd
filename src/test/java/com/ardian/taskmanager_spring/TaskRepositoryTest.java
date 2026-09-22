package com.ardian.taskmanager_spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;


import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private ProjektRepository projektRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testTaskMitProjektVerknuepfen() {
        Projekt projekt = new Projekt();
        projekt.setName("Website Relaunch");

        Projekt gespeichertesProjekt = projektRepository.save(projekt);

        assertEquals("Website Relaunch", gespeichertesProjekt.getName());

        Task task = new Task();
        task.setTitel("Design erstellen");
        task.setStatus("in Arbeit");
        task.setProjekt(gespeichertesProjekt);

        Task gespeicherteTask = taskRepository.save(task);

        assertEquals("Design erstellen", gespeicherteTask.getTitel());
        assertEquals("in Arbeit", gespeicherteTask.getStatus());
        assertEquals("Website Relaunch", gespeicherteTask.getProjekt().getName());
    }


}
