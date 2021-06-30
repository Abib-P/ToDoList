package io.todolist.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TaskControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void should_create_a_valid_task_and_denied_the_creation_of_the_second_because_too_early() throws Exception {
        this.mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content("""
                        {
                            "email" : "email@email.com",
                            "lastname" : "lastname",
                            "firstname" : "firstname",
                            "birthdate" : "2000-08-16",
                            "password" : "password"
                        }"""))
                .andExpect(status().is(200));

        this.mockMvc.perform(get("/users/email@email.com")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "email" : "email@email.com",
                            "lastname" : "lastname",
                            "firstname" : "firstname",
                            "birthdate" : "2000-08-16",
                            "password" : "password"
                        }"""));

        this.mockMvc.perform(post("/users/email@email.com/tasks")
                .contentType(APPLICATION_JSON)
                .content("""
                        {
                            "name" : "taskName",
                            "content" : "content"
                        }"""))
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/users/email@email.com/tasks")
                .contentType(APPLICATION_JSON)
                .content("""
                        {
                            "name" : "taskName2",
                            "content" : "content"
                        }"""))
                .andExpect(status().is(425))
                .andExpect(content().json("{\"message\"=\"Cannot create new task within 30 minutes of the last task created.\"}"));


        this.mockMvc.perform(get("/users/email@email.com/tasks"))
                .andExpect(content().json("""
                        [{
                                "name" : "taskName",
                                "content" : "content"
                        }]"""))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_an_error_when_trying_to_create_task_with_same_name() throws Exception {
        this.mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content("""
                        {
                            "email" : "email@email.com",
                            "lastname" : "lastname",
                            "firstname" : "firstname",
                            "birthdate" : "2000-08-16",
                            "password" : "password"
                        }"""))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/users/email@email.com")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "email" : "email@email.com",
                            "lastname" : "lastname",
                            "firstname" : "firstname",
                            "birthdate" : "2000-08-16",
                            "password" : "password"
                        }"""));

        this.mockMvc.perform(post("/users/email@email.com/tasks")
                .contentType(APPLICATION_JSON)
                .content("""
                        {
                            "name" : "taskName",
                            "content" : "content"
                        }"""))
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/users/email@email.com/tasks")
                .contentType(APPLICATION_JSON)
                .content("""
                        {
                            "name" : "taskName",
                            "content" : "content"
                        }"""))
                .andExpect(status().is(409))
                .andExpect(content().json("{\"message\"=\"Task name is already taken.\"}"));


        this.mockMvc.perform(get("/users/email@email.com/tasks"))
                .andExpect(content().json("""
                        [{
                                "name" : "taskName",
                                "content" : "content"
                        }]"""))
                .andExpect(status().isOk());
    }

}