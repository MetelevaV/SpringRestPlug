package org.example.kafkaplug.model;

import lombok.Data;

@Data
public class UserMessage {
    private String login;
    private String password;
    private String status;
    private String date;
}
