package org.example.springrestplug.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ResponseData {

    @NotBlank(message="The login must be set")
    @Size(min = 3,message="The name must be at least 3 characters long.")
    private String login;

    @NotBlank(message="The password must be set")
    @Size(min = 3, message="The password must be at least 4 characters long.")
    private String password;

    private String date;

    public String getDate() {
        LocalDateTime newDate = LocalDateTime.now();
        this.date = newDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return date;
    }
}