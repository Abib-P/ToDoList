package io.todolist.api.controller;

import io.todolist.server.servise.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    UserController controller;

    @BeforeEach
    void setup(){
        controller = new UserController(service);
    }

    @Test
    public void should_return_success_when_create_user() throws Exception {


        this.mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content("""
                        {
                            "email" : "email@email.com",
                            "lastname" : "lastname",
                            "firstname" : "firstname",
                            "birthdate" : "2002-02-12",
                            "password" : "password"
                        }"""))
                .andExpect(status().isOk());
    }

    @Test
    public void should_return_400_when_bad_formatted_json_is_send() throws Exception {
        this.mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content("{\"\"}"))
                .andExpect(status().isBadRequest());
    }

}