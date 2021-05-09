package io.todolist.server.user;


import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TaskTest {

    @Test
    public void should_create_task_without_given_date() {
        Task task = new Task("name", "content");

        assertEquals(task.getName(), "name");
        assertEquals(task.getContent(), "content");
        assertThat(task.getCreationDate()).isBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now());
    }

    @Test
    public void should_create_task_with_given_date() {
        LocalDateTime date = LocalDateTime.now();
        Task task = new Task("name", "content", date);

        assertEquals(task.getName(), "name");
        assertEquals(task.getContent(), "content");
        assertThat(task.getCreationDate()).isEqualTo(date);
    }
}

