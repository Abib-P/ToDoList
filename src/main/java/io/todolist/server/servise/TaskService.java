package io.todolist.server.servise;

import io.todolist.server.exception.*;
import io.todolist.server.repository.UserRepositoryInMemory;
import io.todolist.server.user.Task;
import io.todolist.server.user.User;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final UserRepositoryInMemory userRepositoryInMemory;

    private final EmailSenderService emailSenderService;

    TaskService(UserRepositoryInMemory userRepositoryInMemory, EmailSenderService emailSenderService) {
        this.userRepositoryInMemory = userRepositoryInMemory;
        this.emailSenderService = emailSenderService;
    }

    public List<Task> getTasksOfUser(String email) {
        User user = userRepositoryInMemory.getUsers().stream()
                .filter(user1 -> user1.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(email));
        return user.getTasks();
    }

    public Task getTaskOfUser(String email, String name) {
        User user = userRepositoryInMemory.getUsers().stream()
                .filter(user1 -> user1.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(email));
        return user.getTasks().stream().filter((task) -> task.getName().equals(name)).findFirst().orElseThrow(() -> new TaskNotFoundException(name));
    }

    public void addTaskToUser(String email, Task task) {
        User user = userRepositoryInMemory.getUsers().stream()
                .filter(user1 -> user1.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(email));
        if (user.getTasks().size() >= 10) {
            throw new TooManyTasksInToDoListException();
        }
        Optional<Task> optionalTask = user.getTasks().stream().filter(task1 -> task1.getName().equals(task.getName())).findFirst();
        if (optionalTask.isPresent()) {
            throw new TaskNameAlreadyTakenException();
        }
        if (task.getContent().length() > 1000) {
            throw new TaskContentHasTooManyCharactersException();
        }
        optionalTask = user.getTasks().stream()
                .reduce((task1, task2) -> {
                    if (task1.getCreationDate().isAfter(task2.getCreationDate())) {
                        return task1;
                    }
                    return task2;
                });
        if (optionalTask.isPresent() && Duration.between(optionalTask.get().getCreationDate(), task.getCreationDate()).toMinutes() <= 30) {
            throw new TaskCreationUnderThirtyMinuteException();
        }
        user.addTask(task);

        if (user.getTasks().size() == 8) {
            emailSenderService.sendEmailTo(user);
        }
    }

    public void deleteTaskOfUser(String email, String name) {
        User user = userRepositoryInMemory.getUsers().stream()
                .filter(user1 -> user1.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(email));
        user.deleteTask(name);
    }

    //update task
}
