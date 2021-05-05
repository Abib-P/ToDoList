package io.todolist.server.user;

import io.todolist.server.repository.TaskRepository;

import java.time.LocalDate;

public class User {

    private final String email;
    private final String lastname;
    private final String firstname;
    private final LocalDate birthdate;
    private final String password;

    private final TaskRepository taskRepository;

    public User(String email, String lastname, String firstname, LocalDate birthdate, String password){
        this.email = email;
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.password = password;
        this.taskRepository = new TaskRepository();
    }

    public String getEmail() {
        return email;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getPassword() {
        return password;
    }
}
