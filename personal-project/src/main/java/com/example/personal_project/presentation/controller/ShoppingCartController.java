package com.example.personal_project.presentation.controller;

import com.example.personal_project.application.ShoppingCartService;
import com.example.personal_project.presentation.dto.ShoppingCartRequest;
import com.example.personal_project.presentation.dto.ShoppingCartResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/shopping-cart")
    public ShoppingCartResponse addCart(@Valid @RequestBody ShoppingCartRequest shoppingCartRequest){
        return shoppingCartService.addCart(shoppingCartRequest);
    }

}
