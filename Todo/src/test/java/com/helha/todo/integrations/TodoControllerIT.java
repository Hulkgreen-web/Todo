package com.helha.todo.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helha.todo.infrastructure.todo.DbTodo;
import com.helha.todo.infrastructure.todo.ITodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
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

    DbTodo persist(String title, boolean done){
        DbTodo t = new DbTodo();
        t.title=title;
        t.done=done;
        return todoRepository.save(t);
    }

    @Nested
    @DisplayName("GET /todos")
    class GetTodos {
        @Test
        @DisplayName("200-retourne une liste vide de todo")
        void getTodos_shouldReturnEmptyList() throws Exception {
            mockMvc.perform(get("/todos")).andExpect(status().isOk())
                    .andExpect(jsonPath("$.todos",hasSize(0)));
        }



        @Test
        @DisplayName("200 - returne une liste avec 2 todo")
        void getTodos_shouldReturnListOfTodos() throws Exception {
            persist("title",true);
            persist("done",false);
            mockMvc.perform(get("/todos")).andExpect(status().isOk())
                    .andExpect(jsonPath("$.todos",hasSize(2)))
                    .andExpect(jsonPath("$.todos[0].id").isNumber())
                    .andExpect(jsonPath("$.todos[0].title").isString())
                    .andExpect(jsonPath("$.todos[0].done").isBoolean());
        }
    }

    @Nested
    @DisplayName("GET /todos/{id}")
    class GetTodoById {
        @Test
        @DisplayName("404- todo pas found")
        void getTodoById_shouldReturn404() throws Exception {
            mockMvc.perform(get("/todos/{id}", 1))
                    .andExpect(status().isNotFound());
        }
    }
/*
    @Nested
    @DisplayName("POST/todos")
    class AddTodo {
        @Test
        @DisplayName("201-todo crée")
        void addTodo_shouldReturnCreated() throws Exception {
            Todo todo = new Todo();
            todo.setTitle("etudier Mamadou");
            todo.setDone(false);

            String payload = objectMapper.writeValueAsString(todo);
            mockMvc.perform(
                    post("/todos")
            .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.title").value("etudier Mamadou"))
                    .andExpect(jsonPath("$.done").value(false));
        }
        @Test
        @DisplayName("400-title est vide")
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
    @Nested
    @DisplayName("PUT /todos")
    class UpdateTodo {
        @Test
        @DisplayName("200 - met à jour un todo existant")
        void update_ok() throws Exception {
            Todo current = persist("Init", false);
            String newTitle = "Titre mis à jour";
            TodoController.UpdateTodoRequest request = new TodoController.UpdateTodoRequest(
                    current.getId(), newTitle, true
            );

            String payload = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/todos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(current.getId()))
                    .andExpect(jsonPath("$.title").value(newTitle))
                    .andExpect(jsonPath("$.done").value(true));
        }

        @Test
        @DisplayName("400 - validation KO si title vide")
        void update_validationError() throws Exception {
            Todo current = persist("Init", false);

            TodoController.UpdateTodoRequest request = new TodoController.UpdateTodoRequest(
                    current.getId(), "", true
            );

            String payload = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/todos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("404 - todo à mettre à jour introuvable")
        void update_notFound() throws Exception {
            TodoController.UpdateTodoRequest request = new TodoController.UpdateTodoRequest(
                    1000, "hello", true
            );

            String payload = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/todos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /todos/{id}")
    class DeleteTodo {
        @Test
        @DisplayName("204 - supprime un todo existant")
        void delete_noContent() throws Exception {
            Todo t = persist("À supprimer", false);

            mockMvc.perform(delete("/todos/{id}", t.getId()))
                    .andExpect(status().isNoContent());

            mockMvc.perform(get("/todos/{id}", t.getId()))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("404 - suppression d'un id inexistant")
        void delete_notFound() throws Exception {
            mockMvc.perform(delete("/todos/{id}", 9999))
                    .andExpect(status().isNotFound());
        }
    }


 */
}
