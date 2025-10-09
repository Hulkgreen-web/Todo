package com.helha.todo.infrastructure;

import com.helha.todo.domain.Todo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITodoRepository extends CrudRepository<Todo, Long> {

    Iterable<Todo> findByTitle(String title);

    Iterable<Todo> findByTitleContainingIgnoreCase(String title);

    Iterable<Todo> findBySavedAndDone(boolean saved, boolean done);
}
