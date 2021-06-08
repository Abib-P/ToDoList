package io.todolist.server.repository;

import io.todolist.server.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

class UserRepositoryInMemoryTest {

    UserRepositoryInMemory userRepositoryInMemory;

    @BeforeEach
    void setup() {
        userRepositoryInMemory = new UserRepositoryInMemory();
    }

    @Test
    void should_list_be_empty_when_first_start() {
        Assertions.assertEquals(userRepositoryInMemory.getUsers().size(), 0);
    }

    @Test
    void should_add_user_to_list() {
        User user1 = new User("test", "", "", LocalDate.now(), "");
        User user2 = new User("test2", "", "", LocalDate.now(), "");

        userRepositoryInMemory.addUser(user1);
        userRepositoryInMemory.addUser(user2);

        Assertions.assertEquals(userRepositoryInMemory.getUsers().size(), 2);
        Assertions.assertEquals(userRepositoryInMemory.getUsers().stream().filter((user -> user.getEmail().equals("test"))).findFirst().orElseThrow(), user1);
        Assertions.assertEquals(userRepositoryInMemory.getUsers().stream().filter((user -> user.getEmail().equals("test2"))).findFirst().orElseThrow(), user2);
    }

    @Test
    void should_delete_user() {
        User user1 = new User("test", "", "", LocalDate.now(), "");
        User user2 = new User("test2", "", "", LocalDate.now(), "");

        userRepositoryInMemory.addUser(user1);
        userRepositoryInMemory.addUser(user2);
        userRepositoryInMemory.deleteUserByEmail("test");

        Assertions.assertEquals(userRepositoryInMemory.getUsers().size(), 1);
        Assertions.assertEquals(userRepositoryInMemory.getUsers().stream().filter((user -> user.getEmail().equals("test"))).findFirst(), Optional.empty());
        Assertions.assertEquals(userRepositoryInMemory.getUsers().stream().filter((user -> user.getEmail().equals("test2"))).findFirst().orElseThrow(), user2);
    }
}