package dev.fabiosimones.jbank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping
    public ResponseEntity<String> ok(){
        System.out.println("Inside controller.");
        return ResponseEntity.ok().build();
    }
}
