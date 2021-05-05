package io.todolist.api.controller;

import io.todolist.api.dto.ErrorMessage;
import io.todolist.api.dto.UserDTO;
import io.todolist.server.exception.UserNotFoundException;
import io.todolist.server.servise.UserService;
import io.todolist.server.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    public List<UserDTO> getUsers() {
        return userService.getUsers().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/users/{email}")
    public UserDTO get(@PathVariable @Valid String email) {
        return new UserDTO(userService.getUser(email));
    }

    @PostMapping(value = "/users")
    public void createUser(@RequestBody @Valid UserDTO userDTO) {
        User user = new User(
                userDTO.getEmail(),
                userDTO.getLastname(),
                userDTO.getFirstname(),
                userDTO.getBirthdate(),
                userDTO.getPassword()
        );
        userService.createUser(user);
    }

    @DeleteMapping(value = "/users/{email}")
    public UserDTO deleteUser(@PathVariable @Valid String email) {
        return new UserDTO(userService.deleteUser(email));
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<ErrorMessage> manageUserNotFound(UserNotFoundException userNotFoundException){
        ErrorMessage error = new ErrorMessage();
        error.setMessage(userNotFoundException.getMessage());
        return ResponseEntity.status(404).body(error);
    }
}
