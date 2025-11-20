package com.helha.todo.controllers.todos;

import com.helha.todo.application.todo.command.TodoCommandProcessor;
import com.helha.todo.application.todo.command.create.CreateTodoInput;
import com.helha.todo.application.todo.command.create.CreateTodoOutput;
import com.helha.todo.application.todo.query.TodoQueryProcessor;
import com.helha.todo.application.todo.query.getall.GetAllTodoInput;
import com.helha.todo.application.todo.query.getall.GetAllTodoOutput;
import com.helha.todo.application.todo.query.getbyid.GetByIdTodoOutput;
import com.helha.todo.controllers.todos.exceptions.TodoNotFoundException;
import com.helha.todo.infrastructure.todo.ITodoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/todos")
public class TodoCommandController {
    private final TodoCommandProcessor todoCommandProcessor;

    public TodoCommandController(TodoCommandProcessor todoCommandProcessor) {
        this.todoCommandProcessor = todoCommandProcessor;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    headers = @Header(
                            name = "Location",
                            description = "Location of created resource"
                    )
            ),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    })
    @PostMapping()
    public ResponseEntity<CreateTodoOutput> create(@Valid @RequestBody CreateTodoInput input) {
        CreateTodoOutput output = todoCommandProcessor.createTodoHandler.handle(input);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(output.id)
                .toUri();
        return ResponseEntity
                .created(location)
                .body(output);
    }
/*
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    })
    @PutMapping
    public ResponseEntity<Todo> update(@Valid @RequestBody UpdateTodoRequest request) {
        return todoRepository.findById(request.id)
                .map(entity -> {
                    entity.setTitle(request.title);
                    entity.setDone(request.done);
                    Todo saved = todoRepository.save(entity);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", content = @Content),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if (!todoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        todoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // RECORDS --> classe pour mettre variables d'instances en publiques --> utile pour véhiculer
    // des informations
    public record CreateTodoRequest(
            @NotBlank(message = "Title cannot be empty") String title,
            boolean done
    ) {}

    public record UpdateTodoRequest(
            long id,
            @NotBlank(message = "Title cannot be empty") String title,
            boolean done
    ) {}

     */
}
