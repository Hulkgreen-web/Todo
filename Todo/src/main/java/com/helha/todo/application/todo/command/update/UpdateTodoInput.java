package com.helha.todo.application.todo.command.update;

import java.time.LocalDateTime;

public class UpdateTodoInput {
    public long id;
    public String title;
    public boolean done;
    public boolean archived;
    public LocalDateTime completedAt;
}
