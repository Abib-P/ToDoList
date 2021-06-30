package io.todolist.server.repository;

import io.todolist.server.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository {
    List<User> getUsers();
    void addUser(User user);
    User deleteUserByEmail(String email);
}
