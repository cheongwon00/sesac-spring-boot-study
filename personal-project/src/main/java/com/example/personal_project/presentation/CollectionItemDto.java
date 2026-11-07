package com.example.personal_project.presentation;

import jakarta.validation.constraints.NotNull;

public record CollectionItemDto(
        Long id,

        @NotNull
        String name,

        @NotNull
        Integer price) {
}
