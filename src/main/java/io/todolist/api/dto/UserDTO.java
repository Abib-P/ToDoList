package io.todolist.api.dto;

import io.todolist.server.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


public class UserDTO {

    @NotBlank
    private String email;
    @NotBlank
    private String lastname;
    @NotBlank
    private String firstname;
    @NotNull
    private LocalDate birthdate;
    @NotBlank
    private String password;

    public UserDTO() {
    }

    public UserDTO(User user) {
        email = user.getEmail();
        lastname = user.getLastname();
        firstname = user.getFirstname();
        birthdate = user.getBirthdate();
        password = user.getPassword();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}
