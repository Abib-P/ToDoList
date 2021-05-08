package io.todolist.server.user;

import io.todolist.server.exception.TaskNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {

    private final String email;
    private final String lastname;
    private final String firstname;
    private final LocalDate birthdate;
    private final String password;

    private final List<Task> tasks;

    public User(String email, String lastname, String firstname, LocalDate birthdate, String password) {
        this.email = email;
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.password = password;
        this.tasks = new ArrayList<>();
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

    public List<Task> getTasks() {
        return tasks;
    }

    public boolean isValid() {
        LocalDate localDate = LocalDate.now().minusYears(13);
        String regex = "^(.+)@(.+)$";

        if (!localDate.isAfter(birthdate)) {
            return false;
        }
        if (this.lastname.equals("") || this.firstname.equals("")) {
            return false;
        }
        if (this.password.length() < 8 || this.password.length() > 40){
            return false;
        }
        if (!this.email.matches(regex)) {
            return false;
        }

        return true;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(String name) {
        Task task = tasks.stream()
                .filter((task1)-> task1.getName().equals(name))
                .findFirst().orElseThrow(()-> new TaskNotFoundException(name));
        tasks.remove(task);
    }
}
