package com.helha.todo.infrastructure.todo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "items")

public class DbTodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String title;
    public boolean done;
    public boolean archived;
    public LocalDateTime completedAt;
}
