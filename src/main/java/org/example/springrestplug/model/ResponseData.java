package org.example.springrestplug.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ResponseData {

    private String login;
    private String password;
    private String date;

    public String getDate() {
        LocalDateTime newDate = LocalDateTime.now();
        this.date = newDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return date;
    }
}