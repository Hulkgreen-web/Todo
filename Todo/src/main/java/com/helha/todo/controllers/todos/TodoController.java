package com.helha.todo.controllers.todos;

import com.helha.todo.controllers.todos.exceptions.TodoNotFoundException;
import com.helha.todo.domain.Todo;
import com.helha.todo.infrastructure.ITodoRepository;
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
import java.time.LocalDateTime;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private ITodoRepository todoRepository;

    public TodoController(ITodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Operation(summary = "List all todos")
    @ApiResponse(responseCode = "200")
    @GetMapping()
    public ResponseEntity<Iterable<Todo>> findByKeyword(@RequestParam(required = false) String title) {
        if (title == null || title.isEmpty())
            return ResponseEntity.ok(todoRepository.findAll());
        else {
            return ResponseEntity.ok(todoRepository.findByTitleContainingIgnoreCase(title));
        }
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404",
                    description = "When a todo isn't found",
            content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    })

    @GetMapping("{id}")
    public ResponseEntity<Todo> findById(@PathVariable Long id) {
        if (!todoRepository.existsById(id)) {
            throw new TodoNotFoundException(id);
        }
        return ResponseEntity.ok(todoRepository.findById(id).get());
    }

    @GetMapping("/filter")
    public ResponseEntity<Iterable<Todo>> findBySavedAndDone(@RequestParam boolean saved, @RequestParam boolean done) {
        return ResponseEntity.ok(todoRepository.findBySavedAndDone(saved,done));
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
    public ResponseEntity<Todo> create(@Valid @RequestBody CreateTodoRequest request) {
        // Mapping
        // CreateTodoRequest -> Todo (La "->" est le mapping)
        // CreateTodoRequest = DTO
        // Data Transfer Object
        Todo todoFromRequest = new Todo();
        todoFromRequest.setTitle(request.title);
        todoFromRequest.setDone(request.done);

        if (todoFromRequest.isDone()) {
            todoFromRequest.setDone_date_time(LocalDateTime.now());
        }

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
    @PutMapping()
    public ResponseEntity<Todo> update(@Valid @RequestBody UpdateTodoRequest request) {
        Todo todoFromRequest = new Todo();
        todoFromRequest.setId(request.id);
        todoFromRequest.setTitle(request.title);
        todoFromRequest.setDone(request.done);
        todoFromRequest.setSaved(request.saved);

        if (request.done) {
            todoFromRequest.setDone_date_time(LocalDateTime.now());
        }
        Todo todoUpdated = todoRepository
                .findById(todoFromRequest.getId())
                .map(entity -> {
                    entity.setTitle(todoFromRequest.getTitle());
                    entity.setDone(todoFromRequest.isDone());
                    if (todoFromRequest.isDone()) {
                        entity.setDone_date_time(LocalDateTime.now());
                    } else {
                        entity.setDone_date_time(null);
                    }
                    entity.setSaved(todoFromRequest.isSaved());
                    return todoRepository.save(entity);
                }).get();
        return ResponseEntity.ok(todoUpdated);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", content = @Content),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        todoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // RECORDS --> classe pour mettre variables d'instances en publiques --> utile pour véhiculer
    // des informations
    public record CreateTodoRequest(
            @NotBlank(message = "Title cannot be empty")
            String title,
            boolean done
    ) {}

    public record UpdateTodoRequest(
            long id,
            @NotBlank(message = "Title cannot be empty") String title,
            boolean done,
            boolean saved
    ) {}
}
