package io.todolist.api.controller;

import io.todolist.server.exception.*;
import io.todolist.server.servise.TaskService;
import io.todolist.server.user.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService service;

    TaskController controller;

    @BeforeEach
    void setup() {
        controller = new TaskController(service);
    }

    @Nested
    class GetTasksEndPoint {

        @Test
        public void should_return_empty_list_when_no_tasks() throws Exception {
            given(service.getTasksOfUser("email")).willReturn(List.of());
            mockMvc.perform(get("/users/email/tasks"))
                    .andExpect(content().json("[]"))
                    .andExpect(status().isOk());
        }

        @Test
        public void should_return_not_empty_list_when_multiple_tasks_are_in_base() throws Exception {
            Task task = new Task("taskName", "content", LocalDateTime.of(2020, 3, 21, 4, 42));
            Task task2 = new Task("taskName2", "content2", LocalDateTime.of(2020, 3, 21, 4, 42));
            given(service.getTasksOfUser("email")).willReturn(List.of(task, task2));
            mockMvc.perform(get("/users/email/tasks"))
                    .andExpect(content().json("""
                            [{
                                    "name" : "taskName",
                                    "content" : "content"
                                },
                                {
                                    "name" : "taskName2",
                                    "content" : "content2"
                            }]"""))
                    .andExpect(status().isOk());
        }

    }

    @Nested
    class GetTaskByNameEndPoint {

        @Test
        public void should_return_task_when_good_email_and_task_name_is_sent() throws Exception {
            Task task = new Task("taskName", "content");
            given(service.getTaskOfUser("email@email.com", "taskName")).willReturn(task);
            mockMvc.perform(get("/users/email@email.com/tasks/taskName"))
                    .andExpect(content().json("""
                            {
                                "name" : "taskName",
                                "content" : "content"
                            }"""))
                    .andExpect(status().isOk());
        }

        @Test
        public void should_404_when_searched_user_doesn_t_exist() throws Exception {
            given(service.getTaskOfUser("email", "taskName")).willThrow(new UserNotFoundException("email"));
            mockMvc.perform(get("/users/email/tasks/taskName"))
                    .andExpect(content().json("{\"message\" : \"User with email: 'email' not found\"}"))
                    .andExpect(status().is(404));
        }

        @Test
        public void should_404_when_searched_task_doesn_t_exist() throws Exception {
            given(service.getTaskOfUser("email", "taskName")).willThrow(new TaskNotFoundException("taskName"));
            mockMvc.perform(get("/users/email/tasks/taskName"))
                    .andExpect(content().json("{\"message\" : \"Task with name: 'taskName' not found\"}"))
                    .andExpect(status().is(404));
        }

    }

    @Nested
    class PostTaskEndPoint {

        @Test
        public void should_return_success_when_create_task() throws Exception {
            mockMvc.perform(post("/users/email/tasks")
                    .contentType(APPLICATION_JSON)
                    .content("""
                            {
                                "name" : "taskName",
                                "content" : "content"
                            }"""))
                    .andExpect(status().isOk());
        }

        @Test
        public void should_return_400_when_bad_formatted_json_is_send() throws Exception {
            mockMvc.perform(post("/users/email/tasks")
                    .contentType(APPLICATION_JSON)
                    .content("{\"\"}"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        public void should_return_404_when_user_is_not_found() throws Exception {
            Task task = new Task("taskName", "content");
            Mockito.doThrow(new UserNotFoundException("email")).when(service).addTaskToUser("email",task);
            mockMvc.perform(post("/users/email/tasks")
                    .contentType(APPLICATION_JSON)
                    .content("""
                            {
                                "name" : "taskName",
                                "content" : "content"
                            }"""))
                    .andExpect(status().is(404))
                    .andExpect(content().json("{\"message\"=\"User with email: 'email' not found\"}"));
        }

        @Test
        public void should_return_418_when_task_content_is_too_long() throws Exception {
            Task task = new Task("taskName", "contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent");
            Mockito.doThrow(new TaskContentHasTooManyCharactersException()).when(service).addTaskToUser("email",task);
            mockMvc.perform(post("/users/email/tasks")
                    .contentType(APPLICATION_JSON)
                    .content("""
                            {
                                "name" : "taskName",
                                "content" : "contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent"
                            }"""))
                    .andExpect(status().is(418))
                    .andExpect(content().json("{\"message\"=\"Task content has too many characters. (maximum characters is 1000)\"}"));
        }

        @Test
        public void should_return_425_when_try_to_create_multiple_task_under_30_minutes() throws Exception {
            Task task = new Task("taskName", "content");
            Mockito.doThrow(new TaskCreationUnderThirtyMinuteException()).when(service).addTaskToUser("email",task);
            mockMvc.perform(post("/users/email/tasks")
                    .contentType(APPLICATION_JSON)
                    .content("""
                            {
                                "name" : "taskName",
                                "content" : "content"
                            }"""))
                    .andExpect(status().is(425))
                    .andExpect(content().json("{\"message\"=\"Cannot create new task within 30 minutes of the last task created.\"}"));
        }

        @Test
        public void should_return_409_when_task_name_is_already_taken() throws Exception {
            Task task = new Task("taskName", "content");
            Mockito.doThrow(new TaskNameAlreadyTakenException()).when(service).addTaskToUser("email",task);
            mockMvc.perform(post("/users/email/tasks")
                    .contentType(APPLICATION_JSON)
                    .content("""
                            {
                                "name" : "taskName",
                                "content" : "content"
                            }"""))
                    .andExpect(status().is(409))
                    .andExpect(content().json("{\"message\"=\"Task name is already taken.\"}"));
        }

        @Test
        public void should_return_426_when_10_task_are_already_in_todo_list() throws Exception {
            Task task = new Task("taskName", "content");
            Mockito.doThrow(new TooManyTasksInToDoListException()).when(service).addTaskToUser("email",task);
            mockMvc.perform(post("/users/email/tasks")
                    .contentType(APPLICATION_JSON)
                    .content("""
                            {
                                "name" : "taskName",
                                "content" : "content"
                            }"""))
                    .andExpect(status().is(426))
                    .andExpect(content().json("{\"message\"=\"Cannot add supplementary task. (maximum task for a user is 10)\"}"));
        }
    }

    @Nested
    class DeleteTaskEndPoint {

        @Test
        public void should_return_success_when_delete_user() throws Exception {
            mockMvc.perform(delete("/users/email@email.com/tasks/task"))
                    .andExpect(status().isOk());
        }

        @Test
        public void should_404_when_searched_user_doesn_t_exist() throws Exception {
            Mockito.doThrow(new UserNotFoundException("email")).when(service).deleteTaskOfUser("email@email.com","task");
            mockMvc.perform(delete("/users/email@email.com/tasks/task"))
                    .andExpect(content().json("{\"message\" : \"User with email: 'email' not found\"}"))
                    .andExpect(status().is(404));
        }

        @Test
        public void should_404_when_searched_task_doesn_t_exist() throws Exception {
            Mockito.doThrow(new TaskNotFoundException("taskName")).when(service).deleteTaskOfUser("email@email.com","task");
            mockMvc.perform(delete("/users/email@email.com/tasks/task"))
                    .andExpect(content().json("{\"message\" : \"Task with name: 'taskName' not found\"}"))
                    .andExpect(status().is(404));
        }
    }
}