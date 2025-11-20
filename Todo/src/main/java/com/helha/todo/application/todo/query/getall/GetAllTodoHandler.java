package com.helha.todo.application.todo.query.getall;

import com.helha.todo.application.utils.IQueryHandler;
import com.helha.todo.infrastructure.todo.DbTodo;
import com.helha.todo.infrastructure.todo.ITodoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllTodoHandler implements IQueryHandler<GetAllTodoInput, GetAllTodoOutput> {
    private final ITodoRepository todoRepository;
    private final ModelMapper modelMapper;

    public GetAllTodoHandler(ITodoRepository todoRepository, ModelMapper modelMapper) {
        this.todoRepository = todoRepository;
        this.modelMapper = modelMapper;
    }

    public GetAllTodoOutput handle(GetAllTodoInput input) {
        Iterable<DbTodo> dbTodos;
        GetAllTodoOutput output = new GetAllTodoOutput();

        if (input.done != null && input.archived != null){
            dbTodos = todoRepository.findByDoneAndArchived(input.done, input.archived);
        }
        dbTodos = todoRepository.findAll();

        for (DbTodo entity : dbTodos) {
            // MAPPING
            output.todos.add(modelMapper.map(entity, GetAllTodoOutput.Todo.class));
        }
        return output;
    }
}
