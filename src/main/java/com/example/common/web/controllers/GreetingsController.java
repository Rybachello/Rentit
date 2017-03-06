package com.example.common.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by lgarcia on 2/10/2017.
 */
@Controller
public class GreetingsController {
    @GetMapping
    public String sayHello() {
        return "hello";
    }
}
