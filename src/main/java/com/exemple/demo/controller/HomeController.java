package com.exemple.demo.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/api/home")
class HomeController {

    @GetMapping("aaa")
    public String aaa() {
        return "aaa";
    }
}