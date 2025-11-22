package com.helha.todo.application.todo.command;

import com.helha.todo.application.todo.command.create.CreateTodoHandler;
import com.helha.todo.application.todo.command.update.UpdateTodoHandler;
import org.springframework.stereotype.Service;

@Service
public class TodoCommandProcessor {
    public final CreateTodoHandler createTodoHandler;
    public final UpdateTodoHandler updateTodoHandler;

    public TodoCommandProcessor(CreateTodoHandler createTodoHandler, UpdateTodoHandler updateTodoHandler) {
        this.createTodoHandler = createTodoHandler;
        this.updateTodoHandler = updateTodoHandler;
    }
}
