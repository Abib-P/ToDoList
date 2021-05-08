package io.todolist.api.controller;

import io.todolist.api.dto.ErrorMessage;
import io.todolist.api.dto.TaskDTO;
import io.todolist.api.dto.UserDTO;
import io.todolist.server.exception.TaskNotFoundException;
import io.todolist.server.exception.UserNotFoundException;
import io.todolist.server.servise.TaskService;
import io.todolist.server.user.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TaskController {

    TaskService taskService;

    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(value = "/users/{email}/tasks")
    public List<TaskDTO> getAllTasks(@PathVariable @Valid String email) {
        return taskService.getTasksOfUser(email).stream().map(TaskDTO::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/users/{email}/tasks/{name}")
    public TaskDTO getTask(@PathVariable @Valid String email, @PathVariable @Valid String name) {
        return new TaskDTO(taskService.getTaskOfUser(email,name));
    }

    @PostMapping(value = "/users/{email}/tasks")
    public void createTask(@PathVariable @Valid String email, @RequestBody @Valid TaskDTO taskDTO) {
        Task task = new Task(taskDTO.getName(),taskDTO.getContent());
        taskService.addTaskToUser(email,task);
    }

    @DeleteMapping(value = "/users/{email}/tasks/{name}")
    public void deleteTask(@PathVariable @Valid String email, @PathVariable @Valid String name) {
        taskService.deleteTaskOfUser(email,name);;
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<ErrorMessage> manageUserNotFound(UserNotFoundException userNotFoundException){
        ErrorMessage error = new ErrorMessage();
        error.setMessage(userNotFoundException.getMessage());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(value = {TaskNotFoundException.class})
    public ResponseEntity<ErrorMessage> manageTaskNotFound(TaskNotFoundException taskNotFoundException){
        ErrorMessage error = new ErrorMessage();
        error.setMessage(taskNotFoundException.getMessage());
        return ResponseEntity.status(404).body(error);
    }
}
