package org.example.springrestplug.controllers;

import org.example.springrestplug.model.ResponseData;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class DemoController {

    private void addDelay() {
        try {
            Thread.sleep(1000 + (int)(Math.random() * 1001));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    @GetMapping("/getData")
    public String returnLogin() {
        addDelay();
        return "{\"login\":\"Login1\",\"status\":\"ok\"}";
    }

    @PostMapping(value = "/postData", consumes = "application/json", produces = "application/json")
    public ResponseData postData(@RequestBody ResponseData data) {
        addDelay();
        LocalDateTime date = LocalDateTime.now();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return new ResponseData(data.getLogin(), data.getPassword(), formattedDate);
    }
}