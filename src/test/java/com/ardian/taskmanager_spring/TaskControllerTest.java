package com.ardian.taskmanager_spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}