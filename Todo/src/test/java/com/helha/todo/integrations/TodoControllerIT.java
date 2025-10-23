package com.helha.todo.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helha.todo.domain.Todo;
import com.helha.todo.infrastructure.ITodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class TodoControllerIT {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ITodoRepository todoRepository;

    @BeforeEach
    public void setup() {
        todoRepository.deleteAll();
    }

    Todo persist(String title, boolean done){
        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setDone(done);
        return todoRepository.save(todo);
    }

    @Nested
    @DisplayName("GET /todos")
    class GetTodos {
        @Test
        @DisplayName("200 - retourne une liste vide de todo")
        void getTodos_shouldReturnEmptyList() throws Exception {
            mockMvc.perform(get("/todos")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("200 - retourne une liste avec deux todo")
        void getTodos_shouldReturnListOfTodos() throws Exception {
            persist("Etudier SpringBoot", true);
            persist("Faire caca", false);

            mockMvc.perform(get("/todos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id").isNumber())
                    .andExpect(jsonPath("$[0].title").isString())
                    .andExpect(jsonPath("$[0].done").isBoolean());
        }
    }

    @Nested
    @DisplayName("GET /todos/{id}")
    class GetTodoById {
        @Test
        @DisplayName("404 - Todo pas trouvé")
        void getTodoById_shouldReturn404() throws Exception {
            mockMvc.perform(get("/todos/{id}", 1))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /todos")
    class AddTodo {
        @Test
        @DisplayName("201 - todo créé")
        void addTodo_shouldReturnCreated() throws Exception {
            Todo todo = new Todo();
            todo.setTitle("Etudier Mamadou");
            todo.setDone(false);

            String payload = objectMapper.writeValueAsString(todo);

            mockMvc.perform(
                    post("/todos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload)
            )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.title").value("Etudier Mamadou"))
                    .andExpect(jsonPath("$.done").value(false));

        }

        @Test
        @DisplayName("400 - title est vide")
        void addTodo_shouldReturn400() throws Exception {
            Todo todo = new Todo();
            todo.setTitle("");
            todo.setDone(false);

            String payload = objectMapper.writeValueAsString(todo);

            mockMvc.perform(post("/todos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload))
                    .andExpect(status().isBadRequest());
        }
    }

}
