package org.example.springrestplug.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class User {
    @NotBlank(message = "The login must be set")
    @Size(min = 3, message = "The login must be at least 3 characters long.")
    private String login;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 3, message = "Password must be at least 3 characters long")
    private String password;

    private LocalDateTime date;

    public User() {
    }

    public User(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.date = null;
    }

    public User(String login, String email, String password, LocalDateTime date) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.date = date;
    }
}
