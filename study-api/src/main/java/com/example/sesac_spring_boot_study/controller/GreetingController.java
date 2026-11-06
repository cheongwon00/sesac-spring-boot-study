package com.example.sesac_spring_boot_study.controller;

import com.example.sesac_spring_boot_study.service.GreetingService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    private final GreetingService service;

    public GreetingController(GreetingService service) {
        this.service = service;
    }


}
