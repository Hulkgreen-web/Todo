package com.helha.todo.controllers.todos;

import com.helha.todo.application.todo.TodoProcessor;
import com.helha.todo.application.todo.query.getall.GetAllTodoInput;
import com.helha.todo.application.todo.query.getall.GetAllTodoOutput;
import com.helha.todo.application.todo.query.getbyid.GetByIdTodoInput;
import com.helha.todo.application.todo.query.getbyid.GetByIdTodoOutput;
import com.helha.todo.controllers.todos.exceptions.TodoNotFoundException;
import com.helha.todo.domain.Todo;
import com.helha.todo.infrastructure.todo.ITodoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoProcessor todoProcessor;
    private ITodoRepository todoRepository;

    public TodoController(TodoProcessor todoProcessor, ITodoRepository todoRepository) {
        this.todoProcessor = todoProcessor;
        this.todoRepository = todoRepository;
    }

    @Operation(summary = "List all todos")
    @ApiResponse(responseCode = "200")
    @GetMapping()
    public ResponseEntity<GetAllTodoOutput> findAll() {
        GetAllTodoInput input = new GetAllTodoInput();
        return ResponseEntity.ok(todoProcessor.getGetAllTodoHandler().handle(input));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404",
                    description = "When a todo isn't found",
            content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    })

    @GetMapping("{id}")
    public ResponseEntity<GetByIdTodoOutput> findById(@PathVariable Long id) {
        if (!todoRepository.existsById(id)) {
            throw new TodoNotFoundException(id);
        }
        GetByIdTodoInput input = new GetByIdTodoInput();
        return ResponseEntity.ok(todoProcessor.getGetByIdTodoHandler().handle(input));
    }
/*
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
    public ResponseEntity<Todo> create(@Valid @RequestBody CreateTodoRequest request) {
        // Mapping
        // CreateTodoRequest -> Todo (La "->" est le mapping)
        // CreateTodoRequest = DTO
        // Data Transfer Object
        Todo todoFromRequest = new Todo();
        todoFromRequest.setTitle(request.title);
        todoFromRequest.setDone(request.done);

        Todo todoSaved=todoRepository.save(todoFromRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(todoSaved.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(todoSaved);
    }

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
