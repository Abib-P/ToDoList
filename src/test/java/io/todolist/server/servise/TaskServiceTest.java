package io.todolist.server.servise;

import io.todolist.server.exception.TaskContentHasTooManyCharactersException;
import io.todolist.server.exception.TaskCreationUnderThirtyMinuteException;
import io.todolist.server.exception.TaskNameAlreadyTakenException;
import io.todolist.server.exception.TooManyTasksInToDoListException;
import io.todolist.server.repository.UserRepository;
import io.todolist.server.user.Task;
import io.todolist.server.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    TaskService taskService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailSenderService emailSenderService;

    @BeforeEach
    void setup() {
        taskService = new TaskService(userRepository, emailSenderService);
    }

    @Test
    void should_throw_exception_given_a_new_task_when_10_tasks_are_register() {
        User user = new User("email", null, null, null, null);
        Task task = new Task(null, null);
        for (int i = 0; i < 10; i++) {
            user.addTask(task);
        }
        given(userRepository.getUsers()).willReturn(List.of(user));

        String errorMessage = assertThrows(TooManyTasksInToDoListException.class, () -> {
            taskService.addTaskToUser("email", task);
        }).getMessage();

        assertEquals(errorMessage, "Cannot add supplementary task. (maximum task for a user is 10)");
    }

    @Test
    void should_throw_exception_given_a_new_task_with_name_already_taken() {
        User user = new User("email", null, null, null, null);
        Task task = new Task("task", null);
        user.addTask(task);
        Task task1 = new Task("task", null);
        given(userRepository.getUsers()).willReturn(List.of(user));

        String errorMessage = assertThrows(TaskNameAlreadyTakenException.class, () -> {
            taskService.addTaskToUser("email", task1);
        }).getMessage();

        assertEquals(errorMessage, "Task name is already taken.");
    }

    @Test
    void should_throw_exception_given_a_new_task_with_more_than_1000_characters_in_content() {
        User user = new User("email", null, null, null, null);
        Task task = new Task("task", "a".repeat(1001));
        given(userRepository.getUsers()).willReturn(List.of(user));

        String errorMessage = assertThrows(TaskContentHasTooManyCharactersException.class, () -> {
            taskService.addTaskToUser("email", task);
        }).getMessage();

        assertEquals(errorMessage, "Task content has too many characters. (maximum characters is 1000)");
    }

    @Test
    void should_throw_exception_given_a_new_task_within_30_minutes_delay() {
        User user = new User("email", null, null, null, null);
        Task task = new Task("task", "null");
        user.addTask(task);
        Task task1 = new Task("task1", "null");
        given(userRepository.getUsers()).willReturn(List.of(user));

        String errorMessage = assertThrows(TaskCreationUnderThirtyMinuteException.class, () -> {
            taskService.addTaskToUser("email", task1);
        }).getMessage();

        assertEquals(errorMessage, "Cannot create new task within 30 minutes of the last task created.");
    }

    @Test
    void should_send_email_if_the_new_created_task_is_the_8th() {
        User user = new User("email", null, null, null, null);
        for (int i = 0; i < 7; i++) {
            Task task = new Task("task" + i, "null", LocalDateTime.now().minusDays(1));
            user.addTask(task);
        }
        Task task = new Task("OmegaTaskTheFuck", "null");
        given(userRepository.getUsers()).willReturn(List.of(user));

        taskService.addTaskToUser("email", task);

        verify(emailSenderService, times(1)).sendEmailTo(user);
    }

    @Test
    void should_not_send_email_if_the_new_created_task_is_the_9th() {
        User user = new User("email", null, null, null, null);
        for (int i = 0; i < 8; i++) {
            Task task = new Task("task" + i, "null", LocalDateTime.now().minusDays(1));
            user.addTask(task);
        }
        Task task = new Task("OmegaTaskTheFuck", "null");
        given(userRepository.getUsers()).willReturn(List.of(user));

        taskService.addTaskToUser("email", task);

        verify(emailSenderService, times(0)).sendEmailTo(user);
    }
}