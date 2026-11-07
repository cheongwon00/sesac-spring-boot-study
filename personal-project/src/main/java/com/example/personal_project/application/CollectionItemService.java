package com.example.personal_project.application;

import com.example.personal_project.domain.CollectionItem;
import com.example.personal_project.domain.CollectionItemRepository;
import com.example.personal_project.infrastructure.ListCollectionItemRepository;
import com.example.personal_project.presentation.CollectionItemDto;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class CollectionItemService {
    private final CollectionItemRepository collectionItemRepository;
    private final ValidationService validationService;

    public CollectionItemService(CollectionItemRepository collectionItemRepository, ValidationService validationService) {
        this.collectionItemRepository = collectionItemRepository;
        this.validationService = validationService;
    }

    public CollectionItemDto add(CollectionItemDto collectionItemDto){
        CollectionItem collectionItem = new CollectionItem(collectionItemDto.name().trim(),collectionItemDto.price());
        validationService.checkValid(collectionItem);
        CollectionItem savedCollectionItem = collectionItemRepository.add(collectionItem);
        CollectionItemDto savedCollectionItemDto = new CollectionItemDto(savedCollectionItem.getId(), savedCollectionItem.getName(), savedCollectionItem.getPrice());
        return savedCollectionItemDto;
    }

    public List<CollectionItemDto> findAll(){
        List<CollectionItem> items = collectionItemRepository.findAll();
        return items.stream()
                .map(item -> new CollectionItemDto(item.getId(),item.getName(),item.getPrice()))
                .toList();
    }

    public CollectionItemDto findItemById(Long id){
        CollectionItem findCollectionItem = collectionItemRepository.findById(id);
        CollectionItemDto findCollectionItemDto = new CollectionItemDto(findCollectionItem.getId(), findCollectionItem.getName(), findCollectionItem.getPrice());
        return findCollectionItemDto;
    }

    public List<CollectionItemDto> findItemsWithBudget(Integer budget){
        return collectionItemRepository.findAll().stream()
                .filter(item -> item.getPrice()<=budget)
                .sorted(Comparator.comparingInt(CollectionItem::getPrice))
                .limit(3)
                .map(item -> new CollectionItemDto(item.getId(),item.getName(), item.getPrice()))
                .toList();
    }
}
