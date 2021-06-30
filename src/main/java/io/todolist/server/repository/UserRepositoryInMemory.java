package io.todolist.server.repository;

import io.todolist.server.exception.UserNotFoundException;
import io.todolist.server.user.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryInMemory implements UserRepository{
    List<User> users;

    UserRepositoryInMemory() {
        this.users = new ArrayList<>();
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public User deleteUserByEmail(String email) {
        User userToDelete = users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(email));
        users.remove(userToDelete);
        return userToDelete;
    }
}
