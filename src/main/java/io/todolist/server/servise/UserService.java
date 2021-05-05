package io.todolist.server.servise;

import io.todolist.api.dto.UserDTO;
import io.todolist.server.exception.UserNotFoundException;
import io.todolist.server.repository.UserRepository;
import io.todolist.server.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public void createUser(User user) {
        userRepository.addUser(user);
    }

    public User getUser(String email) {
        return userRepository.getUsers().stream().filter(user->user.getEmail().equals(email)).findFirst().orElseThrow(() -> new UserNotFoundException(email));
    }

    public User deleteUser(String email) {
        return userRepository.deleteUserByEmail(email);
    }

    //Créer un user avec sa list
    //Delete un user avec sa list et tasks
    //update user
}
