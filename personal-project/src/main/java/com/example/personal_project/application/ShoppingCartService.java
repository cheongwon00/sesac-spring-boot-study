package com.example.personal_project.application;

import com.example.personal_project.domain.CollectionItemRepository;
import com.example.personal_project.domain.ShoppingCartRepository;
import com.example.personal_project.domain.entity.CollectionItem;
import com.example.personal_project.domain.entity.ShoppingCart;
import com.example.personal_project.domain.exception.CommonConflictException;
import com.example.personal_project.presentation.dto.ShoppingCartRequest;
import com.example.personal_project.presentation.dto.ShoppingCartResponse;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CollectionItemRepository collectionItemRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, CollectionItemRepository collectionItemRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.collectionItemRepository = collectionItemRepository;
    }

    public ShoppingCartResponse addCart(ShoppingCartRequest shoppingCartRequest){
        CollectionItem collectionItem = collectionItemRepository.findById(shoppingCartRequest.collectionItemId());
        if(shoppingCartRepository.existByCollectionItemId(collectionItem.getId()))
            throw new CommonConflictException("이미 장바구니에 해당 상품이 있습니다.");
        ShoppingCart shoppingCart= ShoppingCart.createShoppingCart(collectionItem,shoppingCartRequest);
        ShoppingCart createdShoppingCart = shoppingCartRepository.add(shoppingCart);
        return ShoppingCartResponse.form(createdShoppingCart);
    }
}
