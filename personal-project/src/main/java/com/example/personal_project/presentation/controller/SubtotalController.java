package com.example.personal_project.presentation.controller;

import com.example.personal_project.application.SubtotalService;
import com.example.personal_project.presentation.dto.shoppingCart.SubtotalRequest;
import com.example.personal_project.presentation.dto.shoppingCart.SubtotalResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubtotalController {
    private final SubtotalService subtotalService;

    public SubtotalController(SubtotalService subtotalService) {
        this.subtotalService = subtotalService;
    }

    @GetMapping("shopping-cart/subtotal")
    public SubtotalResponse getSubtotal(@RequestBody SubtotalRequest subtotalRequest){
        return subtotalService.getSubtotal(subtotalRequest);
    }
}
