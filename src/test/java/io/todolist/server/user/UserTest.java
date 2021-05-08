package io.todolist.server.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class UserTest {

    @Test
    public void should_user_be_valid_given_valid_parameters() {
        User user = new User("test@test.com", "test", "test", LocalDate.now().minusYears(27),"password");

        Assertions.assertTrue( user.isValid());
    }

    @Test
    public void should_user_not_be_valid_given_not_valid_email() {
        User user = new User("d@", "test", "test", LocalDate.now().minusYears(27),"password");

        Assertions.assertFalse(user.isValid());
    }

    @Test
    public void should_user_not_be_valid_given_empty_lastname() {
        User user = new User("test@test.com", "", "test", LocalDate.now().minusYears(27),"password");

        Assertions.assertFalse( user.isValid());
    }

    @Test
    public void should_user_not_be_valid_given_empty_firstname() {
        User user = new User("test@test.com", "test", "", LocalDate.now().minusYears(27),"password");

        Assertions.assertFalse( user.isValid());
    }

    @Test
    public void should_user_not_be_valid_given_age_under_13() {
        User user = new User("test@test.com", "test", "test", LocalDate.now().minusYears(10),"password");

        Assertions.assertFalse( user.isValid());
    }

    @Test
    public void should_user_be_valid_given_age_of_13() {
        User user = new User("test@test.com", "test", "test", LocalDate.now().minusYears(13).minusDays(1),"password");

        Assertions.assertTrue( user.isValid());
    }

    @Test
    public void should_user_not_be_valid_given_password_under_8_characters() {
        User user = new User("test@test.com", "test", "test", LocalDate.now().minusYears(27),"pass");

        Assertions.assertFalse( user.isValid());
    }

    @Test
    public void should_user_not_be_valid_given_password_over_40_characters() {
        User user = new User("test@test.com", "test", "test", LocalDate.now().minusYears(27),"passwordpasswordpasswordpasswordpasswordpass");

        Assertions.assertFalse( user.isValid());
    }
}
