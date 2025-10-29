package com.helha.todo.application.todo.query.getbyid;

import com.helha.todo.domain.Todo;

public class GetByIdTodoOutput {
    public Todo todo = new Todo();

    public static class Todo {
        public long id;
        public String title;
        public boolean done;
    }
}
