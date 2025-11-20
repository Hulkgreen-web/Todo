package com.helha.todo.infrastructure.todo;

import com.helha.todo.domain.Todo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITodoRepository extends CrudRepository<DbTodo, Long> {
    List<DbTodo> findByTitleContainingIgnoreCase(String title);
    List<DbTodo> findByDoneAndArchived(boolean done, boolean archived);
}
