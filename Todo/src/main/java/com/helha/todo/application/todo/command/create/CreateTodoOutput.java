package com.helha.todo.application.todo.command.create;

import java.time.LocalDateTime;

public class CreateTodoOutput {
    public long id;
    public String title;
    public boolean done;
    public boolean archived;
    public LocalDateTime completedAt;
}
