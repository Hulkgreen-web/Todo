package com.helha.todo.application.todo.query.getall;

import com.helha.todo.infrastructure.todo.DbTodo;
import com.helha.todo.infrastructure.todo.ITodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllTodoHandler {
    private final ITodoRepository todoRepository;

    public GetAllTodoHandler(ITodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    public GetAllTodoOutput handle(GetAllTodoInput input) {
        Iterable<DbTodo> dbTodos = todoRepository.findAll();
        GetAllTodoOutput output = new GetAllTodoOutput();

        for (DbTodo entity : dbTodos) {
            // MAPPING
            GetAllTodoOutput.Todo todo = new GetAllTodoOutput.Todo();
            todo.id = entity.id;
            todo.title = entity.title;
            todo.done = entity.done;
            output.todos.add(todo);
        }
        return output;
    }
}
