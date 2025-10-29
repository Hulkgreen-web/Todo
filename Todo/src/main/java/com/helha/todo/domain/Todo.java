package com.helha.todo.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class Todo {
    private long id;
    private String title;
    private boolean done;
}
