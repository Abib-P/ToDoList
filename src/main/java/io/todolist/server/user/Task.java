package io.todolist.server.user;


import java.time.LocalDateTime;

public class Task {
    private final String name;
    private final String content;
    private final LocalDateTime creationDate;

    public Task(String name, String content) {
        this.name = name;
        this.content = content;
        this.creationDate = LocalDateTime.now();
    }

    public Task(String name, String content, LocalDateTime localDateTime) {
        this.name = name;
        this.content = content;
        this.creationDate = localDateTime;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}
