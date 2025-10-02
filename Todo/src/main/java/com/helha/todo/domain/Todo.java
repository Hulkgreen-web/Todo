package com.helha.todo.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor

public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private boolean done;
    private LocalDateTime done_date_time;
}
