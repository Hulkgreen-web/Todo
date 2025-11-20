package com.helha.todo.application.utils;

public interface IQueryHandler<I, O> {
    O handle(I input);
}
