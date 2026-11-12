package com.example.personal_project.application;

import com.example.personal_project.domain.coupon.CouponCode;
import com.example.personal_project.domain.exception.CommonBadRequestException;
import com.example.personal_project.domain.repository.ShoppingCartRepository;
import com.example.personal_project.presentation.dto.shoppingCart.SubtotalRequest;
import com.example.personal_project.presentation.dto.shoppingCart.SubtotalResponse;
import org.springframework.stereotype.Service;

@Service
public class SubtotalService {
    private final ShoppingCartRepository shoppingCartRepository;

    public SubtotalService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public SubtotalResponse getSubtotal(SubtotalRequest subtotalRequest){
        CouponCode couponCode;
        Integer shipping;
        if(subtotalRequest.couponCode() == null){
            couponCode = CouponCode.NONE;
            shipping = 0;
        }else if(subtotalRequest.couponCode().equals("PERCENT10")){
            couponCode = CouponCode.PERCENT10;
            shipping = CouponCode.SHIPPING_FEE;
        }else if(subtotalRequest.couponCode().equals("FIXED15000")){
            couponCode = CouponCode.FIXED15000;
            shipping = CouponCode.SHIPPING_FEE;
        }else
            throw new CommonBadRequestException("존재하지 않는 쿠폰입니다");

        Long subtotal = shoppingCartRepository.findAll().stream()
                .mapToLong(item -> (long) item.getUnitPrice() * item.getAmount())
                .sum();

        Long discount = couponCode.discount(subtotal);
        Long discountedSubtotal = subtotal - discount;
        if(discountedSubtotal>=CouponCode.MIN_COST) shipping=0;
        Long finalPrice = discountedSubtotal + shipping;
        String coupon = subtotalRequest.couponCode();

        return new SubtotalResponse(subtotal,discount,discountedSubtotal,shipping,finalPrice,coupon);
    }
}
