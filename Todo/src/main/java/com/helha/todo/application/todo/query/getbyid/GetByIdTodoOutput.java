package com.helha.todo.application.todo.query.getbyid;

import com.helha.todo.domain.Todo;

import java.time.LocalDateTime;

public class GetByIdTodoOutput {
    public long id;
    public String title;
    public Boolean done;
    public Boolean archived;
    public LocalDateTime completedAt;
}
