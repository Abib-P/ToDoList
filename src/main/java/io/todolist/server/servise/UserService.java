package io.todolist.server.servise;

import io.todolist.server.exception.EmailAlreadyTakenException;
import io.todolist.server.exception.UserNotFoundException;
import io.todolist.server.repository.UserRepository;
import io.todolist.server.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public void addUser(User user) {
        user.userVerification();
        Optional<User> optionalUser = userRepository.getUsers().stream().filter(user1 -> user1.getEmail().equals(user.getEmail())).findFirst();
        if (optionalUser.isPresent()) {
            throw new EmailAlreadyTakenException(user.getEmail());
        }
        userRepository.addUser(user);
    }

    public User getUser(String email) {
        return userRepository.getUsers()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    public User deleteUser(String email) {
        return userRepository.deleteUserByEmail(email);
    }

}
