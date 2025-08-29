package org.example.springrestplug.controllers;

import org.example.springrestplug.model.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DemoController {

    private void addDelay() {
        try {
            Thread.sleep(1000 + (int) (Math.random() * 1001));
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
    public ResponseEntity<ResponseData> postData(@RequestBody ResponseData data) {
        addDelay();

        return ResponseEntity.ok(data);
    }
}