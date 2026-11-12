package com.example.personal_project.domain.coupon;

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

    public Long discount(Long subtotal){
        return couponCalc.calc(subtotal);
    }
}
