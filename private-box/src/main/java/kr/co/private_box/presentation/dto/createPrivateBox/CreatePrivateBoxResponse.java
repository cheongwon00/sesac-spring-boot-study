package kr.co.private_box.presentation.dto.createPrivateBox;

import kr.co.private_box.domain.PrivateBox;

public record CreatePrivateBoxResponse(
        String privateUrl
) {
    public static CreatePrivateBoxResponse from(PrivateBox privateBox){
        return new CreatePrivateBoxResponse(privateBox.getPrivateUrl());
    }
}
