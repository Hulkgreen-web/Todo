package com.helha.todo.application.todo.query;

import com.helha.todo.application.todo.query.getall.GetAllTodoHandler;
import com.helha.todo.application.todo.query.getbyid.GetByIdTodoHandler;
import org.springframework.stereotype.Service;

@Service
public class TodoQueryProcessor {
    public final GetAllTodoHandler getAllTodoHandler;
    public final GetByIdTodoHandler getByIdTodoHandler;

    public TodoQueryProcessor(GetAllTodoHandler getAllTodoHandler, GetByIdTodoHandler getByIdTodoHandler) {
        this.getAllTodoHandler = getAllTodoHandler;
        this.getByIdTodoHandler = getByIdTodoHandler;
    }
}
