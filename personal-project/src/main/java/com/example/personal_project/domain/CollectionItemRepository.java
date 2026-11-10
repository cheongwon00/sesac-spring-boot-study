package com.example.personal_project.domain;

import com.example.personal_project.domain.entity.CollectionItem;

import java.util.List;

public interface CollectionItemRepository {
    CollectionItem add(CollectionItem collectionItem);
    List<CollectionItem> findAll();
    CollectionItem findById(Long id);
    Boolean existsByExternalId(Long externalId);
}
