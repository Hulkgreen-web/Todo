package com.helha.todo.application.todo.command.update;

import com.helha.todo.application.todo.query.getbyid.GetByIdTodoHandler;
import com.helha.todo.application.todo.query.getbyid.GetByIdTodoOutput;
import com.helha.todo.application.utils.IEffectCommandHandler;
import com.helha.todo.controllers.todos.exceptions.TodoNotFoundException;
import com.helha.todo.infrastructure.todo.DbTodo;
import com.helha.todo.infrastructure.todo.ITodoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UpdateTodoHandler implements IEffectCommandHandler<UpdateTodoInput> {
    private final ITodoRepository todoRepository;

    public UpdateTodoHandler(ITodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public void handle(UpdateTodoInput input) {
        todoRepository
                .findById(input.id)
                .map(e -> {
                    if (!e.done && input.done)
                        e.completedAt = LocalDateTime.now();
                    else if (!input.done)
                        e.completedAt = null;

                    e.title = input.title;
                    e.done = input.done;
                    e.archived = input.archived;

                    return todoRepository.save(e);
                }).orElseThrow(() -> new IllegalArgumentException("Todo not found " + input.id));
    }
}
