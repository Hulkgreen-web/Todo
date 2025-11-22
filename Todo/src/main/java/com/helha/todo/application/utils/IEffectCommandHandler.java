package com.helha.todo.application.utils;

public interface IEffectCommandHandler<I> {
    void handle(I input);
}
