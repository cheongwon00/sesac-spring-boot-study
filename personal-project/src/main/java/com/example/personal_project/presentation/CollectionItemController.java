package com.example.personal_project.presentation;

import com.example.personal_project.application.CollectionItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CollectionItemController {
    private final CollectionItemService collectionItemService;

    public CollectionItemController(CollectionItemService collectionItemService) {
        this.collectionItemService = collectionItemService;
    }

    @PostMapping("/item")
    public CollectionItemDto createItem(@Valid @RequestBody CollectionItemDto collectionItemDto){
        return collectionItemService.add(collectionItemDto);
    }

    @GetMapping("/item")
    public List<CollectionItemDto> findItems(){
        return collectionItemService.findAll();
    }

    @GetMapping("/item/{id}")
    public CollectionItemDto findItemById(@PathVariable Long id){
        return collectionItemService.findItemById(id);
    }

    @GetMapping("/item/recommendations")
    public List<CollectionItemDto> findItemsWithBudget(
            @RequestParam
            @Min(value = 0, message = "예산은 0 이상입니다.")
            Integer budget
    ){
        return collectionItemService.findItemsWithBudget(budget);
    }
}
