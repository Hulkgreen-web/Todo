package com.helha.todo.infrastructure.todo;

import jakarta.persistence.*;

@Entity
@Table(name = "items")

public class DbTodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String title;
    public boolean done;
}
