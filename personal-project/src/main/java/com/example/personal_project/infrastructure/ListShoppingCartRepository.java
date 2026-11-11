package com.example.personal_project.infrastructure;

import com.example.personal_project.domain.ShoppingCartRepository;
import com.example.personal_project.domain.entity.ShoppingCart;
import com.example.personal_project.domain.exception.CommonConflictException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class ListShoppingCartRepository implements ShoppingCartRepository {
    List<ShoppingCart> shoppingCarts = new CopyOnWriteArrayList<>();

    public ShoppingCart add(ShoppingCart shoppingCart){
        if(shoppingCarts.size() == ShoppingCart.MAX_CART_ITEM)
            throw new CommonConflictException("장바구니 아이템은 최대 20개입니다");
        shoppingCarts.add(shoppingCart);
        return shoppingCart;
    }

    public Boolean existByCollectionItemId(Long collectionItemId){
        return shoppingCarts.stream()
                .anyMatch(shoppingCart ->
                        Objects.equals(shoppingCart.getCollectionItemId(),collectionItemId));
    }
}
