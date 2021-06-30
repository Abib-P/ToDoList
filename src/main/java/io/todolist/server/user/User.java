package io.todolist.server.user;

import io.todolist.server.exception.NotValidUserException;
import io.todolist.server.exception.TaskNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public void userVerification() {
        String regex = "^(.+)@(.+)$";

        if (!LocalDate.now().minusYears(13).isAfter(birthdate)) {
            throw new NotValidUserException("The age specified must at least be 13. (given age : '"+birthdate+"')");
        }
        if (this.lastname.equals("") || this.firstname.equals("")) {
            throw new NotValidUserException("Firstname and lastname must be specified");
        }
        if (this.password.length() < 8 || this.password.length() > 40){
            throw new NotValidUserException("Password length must be between 8 and 40 characters. (password length : " + password.length() + ")");
        }
        if (!this.email.matches(regex)) {
            throw new NotValidUserException("Email format must be valid. (Example of email : 'email@email.com')");
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(lastname, user.lastname) && Objects.equals(firstname, user.firstname) && Objects.equals(birthdate, user.birthdate) && Objects.equals(password, user.password) && Objects.equals(tasks, user.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, lastname, firstname, birthdate, password, tasks);
    }
}
