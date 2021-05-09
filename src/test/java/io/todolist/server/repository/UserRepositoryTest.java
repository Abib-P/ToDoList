package io.todolist.server.repository;

import io.todolist.server.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class UserRepositoryTest {

    UserRepository userRepository;

    @BeforeEach
    void setup(){
        userRepository = new UserRepository();
    }

    @Test
    void should_list_be_empty_when_first_start(){
        Assertions.assertEquals(userRepository.getUsers().size(),0);
    }

    @Test
    void should_add_user_to_list(){
        User user1 = new User("test","","", LocalDate.now(),"");
        User user2 = new User("test2","","", LocalDate.now(),"");

        userRepository.addUser(user1);
        userRepository.addUser(user2);

        Assertions.assertEquals(userRepository.getUsers().size(), 2);
        Assertions.assertEquals(userRepository.getUsers().stream().filter((user -> user.getEmail().equals("test"))).findFirst().orElseThrow(), user1);
        Assertions.assertEquals(userRepository.getUsers().stream().filter((user -> user.getEmail().equals("test2"))).findFirst().orElseThrow(), user2);
    }

    @Test
    void should_delete_user(){
        User user1 = new User("test","","", LocalDate.now(),"");
        User user2 = new User("test2","","", LocalDate.now(),"");

        userRepository.addUser(user1);
        userRepository.addUser(user2);
        userRepository.deleteUserByEmail("test");

        Assertions.assertEquals(userRepository.getUsers().size(), 1);
        Assertions.assertEquals(userRepository.getUsers().stream().filter((user -> user.getEmail().equals("test"))).findFirst(), Optional.empty());
        Assertions.assertEquals(userRepository.getUsers().stream().filter((user -> user.getEmail().equals("test2"))).findFirst().orElseThrow(), user2);
    }
}