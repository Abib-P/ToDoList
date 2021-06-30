package io.todolist.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
class UserControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void should_create_2_user_and_displays_them() throws Exception {
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

        this.mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content("""
                        {
                            "email" : "email2@email2.com2",
                            "lastname" : "lastname2",
                            "firstname" : "firstname2",
                            "birthdate" : "2002-02-12",
                            "password" : "password2"
                        }"""))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/users")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
        .andExpect(content().json("""
                [{
                    "email" : "email2@email2.com2",
                    "lastname" : "lastname2",
                    "firstname" : "firstname2",
                    "birthdate" : "2002-02-12",
                    "password" : "password2"
                },{
                    "email" : "email@email.com",
                    "lastname" : "lastname",
                    "firstname" : "firstname",
                    "birthdate" : "2000-08-16",
                    "password" : "password"
                }]"""));
    }

    @Test
    public void should_return_400_when_bad_formatted_json_is_send() throws Exception {
        this.mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content("{\"\"}"))
                .andExpect(status().isBadRequest());
    }

}