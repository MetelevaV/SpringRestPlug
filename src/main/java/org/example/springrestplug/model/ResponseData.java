package org.example.springrestplug.model;

import lombok.Data;

@Data
public class ResponseData {

    private String login;
    private String password;
    private String date;

    public ResponseData(String login, String password, String date) {
        this.login = login;
        this.password = password;
        this.date = date;
    }
}