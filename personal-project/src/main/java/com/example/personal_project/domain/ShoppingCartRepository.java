package com.example.personal_project.domain;

import com.example.personal_project.domain.entity.ShoppingCart;

public interface ShoppingCartRepository {
    ShoppingCart add(ShoppingCart shoppingCart);
    Boolean existByCollectionItemId(Long collectionItemId);
}
