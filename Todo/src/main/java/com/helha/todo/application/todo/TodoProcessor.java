package com.helha.todo.application.todo;

import com.helha.todo.application.todo.query.getall.GetAllTodoHandler;
import com.helha.todo.application.todo.query.getbyid.GetByIdTodoHandler;
import org.springframework.stereotype.Service;

@Service
public class TodoProcessor {
    private final GetAllTodoHandler getAllTodoHandler;
    private final GetByIdTodoHandler getByIdTodoHandler;

    public TodoProcessor(GetAllTodoHandler getAllTodoHandler, GetByIdTodoHandler getByIdTodoHandler) {
        this.getAllTodoHandler = getAllTodoHandler;
        this.getByIdTodoHandler = getByIdTodoHandler;
    }

    public GetAllTodoHandler getGetAllTodoHandler() {
        return getAllTodoHandler;
    }

    public GetByIdTodoHandler getGetByIdTodoHandler() {
        return getByIdTodoHandler;
    }
}
