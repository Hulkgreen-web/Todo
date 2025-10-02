package com.helha.todo.controllers.todos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class TodoNotFoundException extends ErrorResponseException {

    public TodoNotFoundException(long id) {
        super(
                HttpStatus.NOT_FOUND,
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.NOT_FOUND,
                        "Todos with %d is not found".formatted(id)
                ),
                null
        );
    }
}
