package com.helha.todo.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor

public class Todo {
    private long id;
    private String title;
    private boolean done;
    private boolean archived;
    private LocalDateTime completedAt;
}
