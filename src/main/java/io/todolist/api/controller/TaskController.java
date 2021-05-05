package io.todolist.api.controller;

import io.todolist.api.dto.TaskDTO;
import io.todolist.server.servise.TaskService;
import io.todolist.server.servise.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TaskController {

    TaskService taskService;

    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(value = "/users/{email}/tasks")
    public List<TaskDTO> getAllTasks(@PathVariable @Valid String email) {
        return null;
    }

    @GetMapping(value = "/users/{email}/tasks/{name}")
    public TaskDTO getTask(@PathVariable @Valid String email, @PathVariable @Valid String name) {
        return null;
    }

    @PostMapping(value = "/users/{email}/tasks/")
    public TaskDTO createTask(@PathVariable @Valid String email, @RequestBody @Valid TaskDTO taskDTO) {
        return null;
    }

    @DeleteMapping(value = "/users/{email}/tasks/{name}")
    public TaskDTO deleteTask(@PathVariable @Valid String email, @PathVariable @Valid String name) {
        return null;
    }
}
