package kr.co.private_box.presentation.dto;

import kr.co.private_box.domain.PrivateBox;
import kr.co.private_box.domain.PrivateBoxStatus;

public record PrivateUrlInformResponse(
        String privateUrl,
        PrivateBoxStatus privateBoxStatus
) {
    public static PrivateUrlInformResponse from(PrivateBox privateBox){
        return new PrivateUrlInformResponse(privateBox.getPrivateUrl(), privateBox.getPrivateBoxStatus());
    }
}
