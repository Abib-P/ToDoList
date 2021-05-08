package io.todolist.server.exception;

public class TaskCreationUnderThirtyMinuteException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Cannot create new task within 30 minutes of the last task created.";
    }
}
