package kr.co.private_box.presentation.dto;

import kr.co.private_box.domain.PrivateBox;

public record VerifyUrlPasswordResponse(
        String originalUrl
) {
    public static VerifyUrlPasswordResponse from(PrivateBox privateBox){
        return new VerifyUrlPasswordResponse(privateBox.getOriginalUrl());
    }
}
