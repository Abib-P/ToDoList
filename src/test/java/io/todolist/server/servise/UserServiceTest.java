package io.todolist.server.servise;

import io.todolist.server.exception.EmailAlreadyTakenException;
import io.todolist.server.exception.NotValidUserException;
import io.todolist.server.repository.UserRepository;
import io.todolist.server.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup(){
        userService = new UserService(userRepository);
    }

    @Test
    void should_refuse_user_creation_with_bad_email_format() {
        User user = new User("email@email","null","null", LocalDate.now().minusYears(51),"nullnull");
        given(userRepository.getUsers()).willReturn(List.of(user,user));

        String errorMessage = assertThrows(EmailAlreadyTakenException.class, ()->userService.addUser(user)).getMessage();

        assertEquals(errorMessage, "Email 'email@email' already taken");
    }

    @Test
    void should_get_user_by_email() {
        User user = new User("email@email","null","null", LocalDate.now().minusYears(51),"nullnull");
        given(userRepository.getUsers()).willReturn(List.of(user));

        User user1 = userService.getUser("email@email");

        assertEquals(user1.getEmail(), "email@email");
    }
}