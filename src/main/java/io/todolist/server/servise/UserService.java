package io.todolist.server.servise;

import io.todolist.server.exception.EmailAlreadyTakenException;
import io.todolist.server.exception.UserNotFoundException;
import io.todolist.server.repository.UserRepositoryInMemory;
import io.todolist.server.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepositoryInMemory userRepositoryInMemory;

    UserService(UserRepositoryInMemory userRepositoryInMemory) {
        this.userRepositoryInMemory = userRepositoryInMemory;
    }

    public List<User> getUsers() {
        return userRepositoryInMemory.getUsers();
    }

    public void addUser(User user) {
        user.userVerification();
        Optional<User> optionalUser = userRepositoryInMemory.getUsers().stream().filter(user1 -> user1.getEmail().equals(user.getEmail())).findFirst();
        if (optionalUser.isPresent()) {
            throw new EmailAlreadyTakenException(user.getEmail());
        }
        userRepositoryInMemory.addUser(user);
    }

    public User getUser(String email) {
        return userRepositoryInMemory.getUsers()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    public User deleteUser(String email) {
        return userRepositoryInMemory.deleteUserByEmail(email);
    }

}
