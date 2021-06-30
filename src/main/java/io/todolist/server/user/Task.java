package io.todolist.server.user;


import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(content, task.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, content);
    }
}
