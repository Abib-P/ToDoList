package io.todolist.server.user;

import io.todolist.server.exception.NotValidUserException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void should_create_user() {
        User user = new User("test@test.com", "test", "test", LocalDate.now().minusYears(27), "password");

        assertEquals(user.getEmail(),"test@test.com");
        assertEquals(user.getLastname(),"test");
        assertEquals(user.getFirstname(),"test");
        assertEquals(user.getBirthdate(),LocalDate.now().minusYears(27));
        assertEquals(user.getPassword(),"password");
    }

    @Test
    public void should_user_be_valid_given_valid_parameters() {
        User user = new User("test@test.com", "test", "test", LocalDate.now().minusYears(27), "password");

        assertDoesNotThrow(user::userVerification);
    }

    @Test
    public void should_user_not_be_valid_given_not_valid_email() {
        User user = new User("d@", "test", "test", LocalDate.now().minusYears(27), "password");

        String errorMessage = assertThrows(NotValidUserException.class, user::userVerification).getMessage();

        assertEquals(errorMessage, "Cannot create new user : Email format must be valid. (Example of email : 'email@email.com')");
    }

    @Test
    public void should_user_not_be_valid_given_empty_lastname() {
        User user = new User("test@test.com", "", "test", LocalDate.now().minusYears(27), "password");

        String errorMessage = assertThrows(NotValidUserException.class, user::userVerification).getMessage();

        assertEquals(errorMessage, "Cannot create new user : Firstname and lastname must be specified");
    }

    @Test
    public void should_user_not_be_valid_given_empty_firstname() {
        User user = new User("test@test.com", "test", "", LocalDate.now().minusYears(27), "password");

        String errorMessage = assertThrows(NotValidUserException.class, user::userVerification).getMessage();

        assertEquals(errorMessage, "Cannot create new user : Firstname and lastname must be specified");
    }

    @Test
    public void should_user_not_be_valid_given_age_under_13() {
        User user = new User("test@test.com", "test", "test", LocalDate.now().minusYears(10), "password");

        String errorMessage = assertThrows(NotValidUserException.class, user::userVerification).getMessage();

        assertEquals(errorMessage, "Cannot create new user : The age specified must at least be 13. (given age : '2011-05-09')");
    }

    @Test
    public void should_user_be_valid_given_age_of_13() {
        User user = new User("test@test.com", "test", "test", LocalDate.now().minusYears(13).minusDays(1), "password");

        assertDoesNotThrow(user::userVerification);
    }

    @Test
    public void should_user_not_be_valid_given_password_under_8_characters() {
        User user = new User("test@test.com", "test", "test", LocalDate.now().minusYears(27), "pass");

        String errorMessage = assertThrows(NotValidUserException.class, user::userVerification).getMessage();

        assertEquals(errorMessage, "Cannot create new user : Password length must be between 8 and 40 characters. (password length : 4)");
    }

    @Test
    public void should_user_not_be_valid_given_password_over_40_characters() {
        User user = new User("test@test.com", "test", "test", LocalDate.now().minusYears(27), "passwordpasswordpasswordpasswordpasswordpass");

        String errorMessage = assertThrows(NotValidUserException.class, user::userVerification).getMessage();

        assertEquals(errorMessage, "Cannot create new user : Password length must be between 8 and 40 characters. (password length : 44)");
    }
}
