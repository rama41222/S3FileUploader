package com.rama41222.Rumj.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class App {

    @Value("${app.message}")
    private String message;

    /**
     * This method return the api name and version.
     * @return message
     */

    @GetMapping("/")
    public String hello(){
        return message;
    }
}
