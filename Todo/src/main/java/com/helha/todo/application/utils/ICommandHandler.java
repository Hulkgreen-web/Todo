package com.helha.todo.application.utils;

public interface ICommandHandler<I, O> {
    O handle(I input);
}
