package com.ardian.taskmanager_spring;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

// Lädt nur die Web-Schicht (Controller, Filter, Security), keine Datenbank
@WebMvcTest(TaskController.class)
// Nimmt die eigene SecurityConfig statt der Spring-Standard-Security
@Import(SecurityConfig.class)
public class TaskControllerTest {

    // Simuliert HTTP-Anfragen, ohne dass ein echter Server läuft
    @Autowired
    private MockMvc mockMvc;

    // Fake-Repository: Der Controller braucht es, aber es gibt keine echte DB
    @MockitoBean
    private TaskRepository taskRepository;

    // Fake-Repository für die Nutzersuche (wird z.B. von der Security gebraucht)
    @MockitoBean
    private NutzerRepository nutzerRepository;

    // Ohne Login darf man nicht an die Tasks: erwartet 401
    @Test
    void testTaskOhneLogin() throws Exception {
        mockMvc.perform(get("/task")).andExpect(status().isUnauthorized());
    }

    // Eingeloggt, aber Task ohne Titel: erwartet 400 aus unserer Validierung
    @Test
    @WithMockUser
    void testTaskOhneTitel() throws Exception {
        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"OFFEN\"}"))
                .andExpect(status().isBadRequest());
    }

    // Eingeloggt als "ich", aber die Task gehört "anderer": erwartet 403
    @Test
    @WithMockUser(username = "ich")
    void testFremdeTaskLoeschen() throws Exception {
        // Der eingeloggte Nutzer, den die Hilfsmethode im Controller per Username sucht
        Nutzer ich = new Nutzer();
        ich.setUsername("ich");
        when(nutzerRepository.findByUsername("ich")).thenReturn(ich);

        // Der Besitzer der Task ist ein anderer Nutzer
        Nutzer anderer = new Nutzer();
        anderer.setUsername("anderer");

        Task task = new Task();
        task.setNutzer(anderer);

        // Das Fake-Repository liefert diese Task, wenn der Controller nach ID 1 sucht
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(delete("/task/1"))
                .andExpect(status().isForbidden());
    }
}