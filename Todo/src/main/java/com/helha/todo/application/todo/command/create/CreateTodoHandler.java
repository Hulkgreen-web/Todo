package com.helha.todo.application.todo.command.create;

import com.helha.todo.application.utils.ICommandHandler;
import com.helha.todo.infrastructure.todo.DbTodo;
import com.helha.todo.infrastructure.todo.ITodoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CreateTodoHandler implements ICommandHandler<CreateTodoInput, CreateTodoOutput> {
    private final ITodoRepository todoRepository;
    private final ModelMapper modelMapper;

    public CreateTodoHandler(ITodoRepository todoRepository, ModelMapper modelMapper) {
        this.todoRepository = todoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CreateTodoOutput handle(CreateTodoInput input) {
        DbTodo entity = modelMapper.map(input, DbTodo.class);
        DbTodo savedEntity = todoRepository.save(entity);
        return modelMapper.map(savedEntity, CreateTodoOutput.class);
    }
}
