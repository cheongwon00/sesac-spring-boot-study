package com.example.personal_project.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class CollectionItem {
    private Long id;

    @Size(min = 1,max = 20)
    private String name;

    @Min(0)
    private Integer price;

    public CollectionItem(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public Boolean sameId(Long id){
        return this.id.equals(id);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
