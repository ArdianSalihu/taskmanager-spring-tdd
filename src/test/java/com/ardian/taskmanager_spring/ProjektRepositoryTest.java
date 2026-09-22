package com.ardian.taskmanager_spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;


import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ProjektRepositoryTest {

    @Autowired
    private ProjektRepository projektRepository;

    @Test
    void testProjektSpeichernUndFinden() {
        Projekt projekt = new Projekt();
        projekt.setName("Website Relaunch");

        Projekt gespeichert = projektRepository.save(projekt);

        assertEquals("Website Relaunch", gespeichert.getName());
    }
}
