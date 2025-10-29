package com.helha.todo.infrastructure.todo;

import com.helha.todo.domain.Todo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITodoRepository extends CrudRepository<DbTodo, Long> {

}
