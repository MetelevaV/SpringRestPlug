package org.example.springrestplug.controllers;

import jakarta.validation.Valid;
import org.example.springrestplug.model.ResponseData;
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
public class Controller {

    private static final List<byte[]> memoryLeakList = new ArrayList<>();

    private void addDelay() {
        try {
            Thread.sleep(1000 + (int) (Math.random() * 1001));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @GetMapping("/getData")
    public ResponseEntity<String> returnLogin() {
        addDelay();
        return ResponseEntity.ok("{\"login\":\"Login1\",\"status\":\"ok\"}");
    }

    @PostMapping(value = "/postData", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseData> postData(@Valid @RequestBody ResponseData data) {
        addDelay();

        return ResponseEntity.ok(data);
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