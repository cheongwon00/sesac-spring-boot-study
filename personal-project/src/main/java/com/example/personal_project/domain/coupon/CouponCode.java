package com.example.personal_project.domain.coupon;

import com.example.personal_project.domain.entity.ShoppingCart;
import com.example.personal_project.domain.exception.CommonBadRequestException;

import java.util.List;

public enum CouponCode {
    NONE(new NoneCalc()),
    PERCENT10(new Percent10Calc()),
    FIXED15000(new Fixed15000Calc());

    private CouponCalc couponCalc;
    public static final int SHIPPING_FEE = 5000;
    public static final int MIN_COST = 100000; // 10만원 이상이면 배송비 무료

    CouponCode(CouponCalc couponCalc) {
        this.couponCalc = couponCalc;
    }

    public static CouponCode createCouponCode(String code){
        if(code == null) return NONE;
        else if(code.equals("PERCENT10")) return PERCENT10;
        else if(code.equals("FIXED15000")) return FIXED15000;
        else throw new CommonBadRequestException("존재하지 않는 쿠폰입니다");
    }

    public Integer getShipping(Long discountedSubtotal){
        if(this == NONE) return 0;
        if(discountedSubtotal>=MIN_COST) return 0;
        return SHIPPING_FEE;
    }

    public static Long calcSubtotal(List<ShoppingCart> shoppingCarts){
        return shoppingCarts.stream()
                .mapToLong(item -> (long) item.getUnitPrice() * item.getAmount())
                .sum();
    }

    public Long discount(Long subtotal){
        return couponCalc.calc(subtotal);
    }
}
