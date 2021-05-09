package io.todolist.server.exception;

public class TooManyTasksInToDoListException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Cannot add supplementary task. (maximum task for a user is 10)";
    }
}
