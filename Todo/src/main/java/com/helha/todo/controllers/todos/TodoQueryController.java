package com.helha.todo.controllers.todos;

import com.helha.todo.application.todo.query.TodoQueryProcessor;
import com.helha.todo.application.todo.query.getall.GetAllTodoInput;
import com.helha.todo.application.todo.query.getall.GetAllTodoOutput;
import com.helha.todo.application.todo.query.getbyid.GetByIdTodoOutput;
import com.helha.todo.controllers.todos.exceptions.TodoNotFoundException;
import com.helha.todo.infrastructure.todo.ITodoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todos")
public class TodoQueryController {
    private final TodoQueryProcessor todoQueryProcessor;

    public TodoQueryController(TodoQueryProcessor todoQueryProcessor) {
        this.todoQueryProcessor = todoQueryProcessor;
    }

    @Operation(summary = "List all todos")
    @ApiResponse(responseCode = "200")
    @GetMapping()
    public ResponseEntity<GetAllTodoOutput> findAll(@RequestParam(required = false) Boolean done,
                                                    @RequestParam(required = false) Boolean archived) {
        GetAllTodoInput input = new GetAllTodoInput(done,archived);
        return ResponseEntity.ok(todoQueryProcessor.getAllTodoHandler.handle(input));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404",
                    description = "When a todo isn't found",
            content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    })

    @GetMapping("{id}")
    public ResponseEntity<GetByIdTodoOutput> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(todoQueryProcessor.getByIdTodoHandler.handle(id));
        } catch (IllegalArgumentException e) {
            throw new TodoNotFoundException(id);
        }
    }
}
