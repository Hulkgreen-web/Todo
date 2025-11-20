package com.helha.todo.application.todo.command;

import com.helha.todo.application.todo.command.create.CreateTodoHandler;
import org.springframework.stereotype.Service;

@Service
public class TodoCommandProcessor {
    public final CreateTodoHandler createTodoHandler;

    public TodoCommandProcessor(CreateTodoHandler createTodoHandler) {
        this.createTodoHandler = createTodoHandler;
    }
}
