package org.example.springrestplug.controllers;

import jakarta.validation.Valid;
import org.example.springrestplug.model.User;
import org.example.springrestplug.repository.DataBaseWorker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class Controller {

    private static final List<byte[]> memoryLeakList = new ArrayList<>();
    private final DataBaseWorker dbWorker;

    // внедрение через конструктор (Spring сам подставит бин)
    public Controller(DataBaseWorker dbWorker) {
        this.dbWorker = dbWorker;
    }

    private void addDelay() {
        try {
            Thread.sleep(1000 + (int) (Math.random() * 1001));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @GetMapping("/{login}")
    public ResponseEntity<?> getUser(@PathVariable String login) {
//        addDelay();
        User user = dbWorker.getUserByLogin(login);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "User with login '" + login + "' not found"));
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/insertUser", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertUser(@Valid @RequestBody User user) {
        addDelay();
        int rows = dbWorker.insertUser(user);

        if (rows == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Insert failed"));
        }

        return ResponseEntity.ok(Map.of("rowsInserted", rows));
    }

    @GetMapping("/leak")
    public ResponseEntity<String> leakMemory() {
        addDelay();
        byte[] leak = new byte[4];
        memoryLeakList.add(leak);

        return ResponseEntity.ok("Add new object: " + memoryLeakList.size());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}