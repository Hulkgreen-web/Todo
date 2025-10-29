package com.helha.todo.application.todo.query.getbyid;

import com.helha.todo.infrastructure.todo.DbTodo;
import com.helha.todo.infrastructure.todo.ITodoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetByIdTodoHandler {
    private final ITodoRepository todoRepository;

    public GetByIdTodoHandler(ITodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public GetByIdTodoOutput handle(GetByIdTodoInput input) {
        Optional<DbTodo> dbTodo = todoRepository.findById(input.id);
        GetByIdTodoOutput output = new GetByIdTodoOutput();

        GetByIdTodoOutput.Todo todo = new GetByIdTodoOutput.Todo();
        todo.id = input.id;
        todo.title = input.title;
        todo.done = input.done;
        output.todo = todo;

        return output;
    }
}
