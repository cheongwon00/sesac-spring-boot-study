package com.example.personal_project.presentation.dto;

import com.example.personal_project.domain.entity.CollectionItem;

public record CollectionItemResponse(Long id, String name, Integer price) {
    public static CollectionItemResponse form(CollectionItem collectionItem){
        return new CollectionItemResponse(
                collectionItem.getId(),
                collectionItem.getName(),
                collectionItem.getPrice());
    }
}
