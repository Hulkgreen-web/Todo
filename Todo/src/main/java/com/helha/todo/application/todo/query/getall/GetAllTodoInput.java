package com.helha.todo.application.todo.query.getall;

public class GetAllTodoInput {

    public Boolean done,archived;

    public GetAllTodoInput(Boolean done, Boolean archived) {
        this.done = done;
        this.archived = archived;
    }
}
