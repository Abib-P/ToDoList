package io.todolist.api.dto;

import javax.validation.constraints.NotBlank;

public class TaskDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String content;

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
