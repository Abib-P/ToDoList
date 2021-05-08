package io.todolist.api.dto;

import io.todolist.server.user.Task;

import javax.validation.constraints.NotBlank;

public class TaskDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String content;

    public TaskDTO() {
    }

    public TaskDTO(Task task) {
        this.name = task.getName();
        this.content = task.getContent();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
