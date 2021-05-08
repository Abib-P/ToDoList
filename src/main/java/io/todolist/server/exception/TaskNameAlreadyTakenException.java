package io.todolist.server.exception;

public class TaskNameAlreadyTakenException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Task name is already taken.";
    }
}
