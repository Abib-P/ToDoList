package io.todolist.server.exception;

import java.util.NoSuchElementException;

public class TaskNotFoundException extends NoSuchElementException {
    private final String name;

    public TaskNotFoundException(final String taskName) {
        this.name = taskName;
    }

    @Override
    public String getMessage() {
        return "Task with name: '" + name + "' not found";
    }
}
