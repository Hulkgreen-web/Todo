package com.helha.todo.application.todo.query.getall;

import java.util.ArrayList;
import java.util.List;

public class GetAllTodoOutput{
    public List<Todo> todos = new ArrayList<>();

    public static class Todo {
        public long id;
        public String title;
        public boolean done;
    }
}
