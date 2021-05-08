package io.todolist.server.exception;

public class NotValidUserException extends RuntimeException{

    private String issue;

    public NotValidUserException(final String issue) {
        this.issue = issue;
    }

    @Override
    public String getMessage() {
        return "Cannot create new user : " + issue ;
    }
}
