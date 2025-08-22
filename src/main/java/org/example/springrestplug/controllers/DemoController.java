package org.example.springrestplug.controllers;

import org.example.springrestplug.model.ResponseData;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class DemoController {


    @GetMapping("/getLogin")
    public String returnLogin() {
        return "{\"login\":\"Login1\",\"status\":\"ok\"}";
    }

    @PostMapping(value = "/postData", consumes = "application/json", produces = "application/json")
    public ResponseData postData(@RequestBody ResponseData data) {
        LocalDateTime date = LocalDateTime.now();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return new ResponseData(data.getLogin(), data.getPassword(), formattedDate);
    }
}