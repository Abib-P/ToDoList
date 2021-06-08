package io.todolist.server.repository;

import io.todolist.server.exception.UserNotFoundException;
import io.todolist.server.user.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryInMemory {
    List<User> users;

    UserRepositoryInMemory() {
        this.users = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User deleteUserByEmail(String email) {
        User userToDelete = users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(email));
        users.remove(userToDelete);
        return userToDelete;
    }
}
