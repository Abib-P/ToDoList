package io.todolist.server.exception;

public class EmailAlreadyTakenException extends RuntimeException{

    private final String email;

    public EmailAlreadyTakenException(final String email) {
        this.email = email;
    }

    @Override
    public String getMessage() {
        return "Email '" + email + "' already taken";
    }
}
