package io.todolist.server.exception;

public class TaskContentHasTooManyCharactersException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Task content has too many characters. (maximum characters is 1000)";
    }
}
