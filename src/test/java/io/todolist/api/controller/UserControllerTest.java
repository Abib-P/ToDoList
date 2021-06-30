package io.todolist.api.controller;

import io.todolist.server.exception.EmailAlreadyTakenException;
import io.todolist.server.exception.NotValidUserException;
import io.todolist.server.exception.UserNotFoundException;
import io.todolist.server.servise.UserService;
import io.todolist.server.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void setup() {
        controller = new UserController(service);
    }

    @Nested
    class GetUsersEndPoint {

        @Test
        public void should_return_empty_list_when_no_users() throws Exception {
            given(service.getUsers()).willReturn(List.of());
            mockMvc.perform(get("/users"))
                    .andExpect(content().json("[]"))
                    .andExpect(status().isOk());
        }

        @Test
        public void should_return_not_empty_list_when_multiple_users_are_in_base() throws Exception {
            User user = new User("email@email.com", "lastname", "firstname", LocalDate.of(2002, 2, 12), "password");
            User user2 = new User("email2@email.com", "lastname2", "firstname2", LocalDate.of(2002, 2, 12), "password2");
            given(service.getUsers()).willReturn(List.of(user, user2));
            mockMvc.perform(get("/users"))
                    .andExpect(content().json("""
                            [   {
                                    "email" : "email@email.com",
                                    "lastname" : "lastname",
                                    "firstname" : "firstname",
                                    "birthdate" : "2002-02-12",
                                    "password" : "password"
                                },
                                {
                                    "email" : "email2@email.com",
                                    "lastname" : "lastname2",
                                    "firstname" : "firstname2",
                                    "birthdate" : "2002-02-12",
                                    "password" : "password2"
                            }]"""))
                    .andExpect(status().isOk());
        }

    }

    @Nested
    class GetUserByEmailEndPoint {

        @Test
        public void should_return_user_when_good_email_is_sent() throws Exception {
            User user = new User("email@email.com", "lastname", "firstname", LocalDate.of(2002, 2, 12), "password");
            given(service.getUser("email@email.com")).willReturn(user);
            mockMvc.perform(get("/users/email@email.com"))
                    .andExpect(content().json("""
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
        public void should_404_when_searched_user_doesn_t_exist() throws Exception {
            given(service.getUser("email")).willThrow(new UserNotFoundException("email@email.com"));
            mockMvc.perform(get("/users/email"))
                    .andExpect(content().json("{\"message\" : \"User with email: 'email@email.com' not found\"}"))
                    .andExpect(status().is(404));
        }

    }

    @Nested
    class PostUserEndPoint {

        @Test
        public void should_return_success_when_create_user() throws Exception {
            mockMvc.perform(post("/users")
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
            mockMvc.perform(post("/users")
                    .contentType(APPLICATION_JSON)
                    .content("{\"\"}"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        public void should_return_409_when_email_already_used_is_send() throws Exception {
            User user = new User("email@email.com", "lastname", "firstname", LocalDate.of(2002, 2, 12), "password");
            Mockito.doThrow(new EmailAlreadyTakenException("email@email.com")).when(service).addUser(user);
            mockMvc.perform(post("/users")
                    .contentType(APPLICATION_JSON)
                    .content("""
                            {
                                "email" : "email@email.com",
                                "lastname" : "lastname",
                                "firstname" : "firstname",
                                "birthdate" : "2002-02-12",
                                "password" : "password"
                            }"""))
                    .andExpect(status().is(409))
                    .andExpect(content().json("{\"message\"=\"Email 'email@email.com' already taken\"}"));
        }

        @Test
        public void should_return_400_when_too_young() throws Exception {
            User user = new User("email@email.com", "lastname", "firstname", LocalDate.of(2012, 2, 12), "password");
            Mockito.doThrow(new NotValidUserException("The age specified must at least be 13. (given age : '"+LocalDate.of(2012, 2, 12)+"')")).when(service).addUser(user);
            mockMvc.perform(post("/users")
                    .contentType(APPLICATION_JSON)
                    .content("""
                            {
                                "email" : "email@email.com",
                                "lastname" : "lastname",
                                "firstname" : "firstname",
                                "birthdate" : "2012-02-12",
                                "password" : "password"
                            }"""))
                    .andExpect(status().is(400))
                    .andExpect(content().json("{\"message\"=\"Cannot create new user : The age specified must at least be 13. (given age : '2012-02-12')\"}"));
        }

        @Test
        public void should_return_400_when_no_lastname_or_firstname() throws Exception {
            User user = new User("email@email.com", "", "", LocalDate.of(2002, 2, 12), "password");
            Mockito.doThrow(new NotValidUserException("Firstname and lastname must be specified")).when(service).addUser(user);
            mockMvc.perform(post("/users")
                    .contentType(APPLICATION_JSON)
                    .content("""
                            {
                                "email" : "email@email.com",
                                "lastname" : "",
                                "firstname" : "",
                                "birthdate" : "2002-02-12",
                                "password" : "password"
                            }"""))
                    .andExpect(status().is(400))
                    .andExpect(content().json("{\"message\"=\"Cannot create new user : Firstname and lastname must be specified\"}"));
        }

        @Test
        public void should_return_400_when_password_has_a_wrong_length() throws Exception {
            User user = new User("email@email.com", "lastname", "firstname", LocalDate.of(2002, 2, 12), "pas");
            Mockito.doThrow(new NotValidUserException("Password length must be between 8 and 40 characters. (password length : " + 3 + ")")).when(service).addUser(user);
            mockMvc.perform(post("/users")
                    .contentType(APPLICATION_JSON)
                    .content("""
                            {
                                "email" : "email@email.com",
                                "lastname" : "lastname",
                                "firstname" : "firstname",
                                "birthdate" : "2002-02-12",
                                "password" : "pas"
                            }"""))
                    .andExpect(status().is(400))
                    .andExpect(content().json("{\"message\"=\"Cannot create new user : Password length must be between 8 and 40 characters. (password length : 3)\"}"));
        }

        @Test
        public void should_return_400_when_email_has_wrong_format() throws Exception {
            User user = new User("email", "lastname", "firstname", LocalDate.of(2002, 2, 12), "pas");
            Mockito.doThrow(new NotValidUserException("Email format must be valid. (Example of email : 'email@email.com')")).when(service).addUser(user);
            mockMvc.perform(post("/users")
                    .contentType(APPLICATION_JSON)
                    .content("""
                            {
                                "email" : "email",
                                "lastname" : "lastname",
                                "firstname" : "firstname",
                                "birthdate" : "2002-02-12",
                                "password" : "pas"
                            }"""))
                    .andExpect(status().is(400))
                    .andExpect(content().json("{\"message\"=\"Cannot create new user : Email format must be valid. (Example of email : 'email@email.com')\"}"));
        }
    }

    @Nested
    class DeleteUserEndPoint {

        @Test
        public void should_return_success_when_delete_user() throws Exception {
            User user = new User("email@email.com", "lastname", "firstname", LocalDate.of(2002, 2, 12), "password");
            given(service.deleteUser("email@email.com")).willReturn(user);
            mockMvc.perform(delete("/users/email@email.com"))
                    .andExpect(content().json("""
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
        public void should_404_when_searched_user_doesn_t_exist() throws Exception {
            given(service.deleteUser("email@email.com")).willThrow(new UserNotFoundException("email@email.com"));
            mockMvc.perform(delete("/users/email@email.com"))
                    .andExpect(content().json("{\"message\" : \"User with email: 'email@email.com' not found\"}"))
                    .andExpect(status().is(404));
        }
    }

}