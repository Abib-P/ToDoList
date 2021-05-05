package io.todolist.server.exception;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {
    private final String email;

    public UserNotFoundException(final String userEmail){
        this.email = userEmail;
    }

    @Override
    public String getMessage(){
        return "User with email: '"+ email +"' not found";
    }
}
