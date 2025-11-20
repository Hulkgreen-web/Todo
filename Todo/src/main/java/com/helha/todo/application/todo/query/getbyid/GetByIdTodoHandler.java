package com.helha.todo.application.todo.query.getbyid;

import com.helha.todo.application.utils.IQueryHandler;
import com.helha.todo.infrastructure.todo.DbTodo;
import com.helha.todo.infrastructure.todo.ITodoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetByIdTodoHandler implements IQueryHandler<Long, GetByIdTodoOutput> {
    private ITodoRepository todoRepository;
    private ModelMapper modelMapper;

    public GetByIdTodoHandler(ITodoRepository todoRepository, ModelMapper modelMapper) {
        this.todoRepository = todoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public GetByIdTodoOutput handle(Long input) {
        Optional<DbTodo> entity = todoRepository.findById(input);

        if (entity.isPresent()) {
            return modelMapper.map(entity.get(), GetByIdTodoOutput.class);
        }

        throw new IllegalArgumentException("Todo not found !");
    }

}
