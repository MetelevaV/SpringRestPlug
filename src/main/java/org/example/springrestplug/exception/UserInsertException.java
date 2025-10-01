package org.example.springrestplug.exception;

public class UserInsertException extends RuntimeException {
    public UserInsertException(String message) {
        super("Failed to insert user: " + message);
    }
}
