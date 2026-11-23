package kr.co.private_box.presentation.dto.createPrivateBox;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record CreatePrivateBoxRequest(
        @NotNull
        @URL(regexp = "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)")
        String originalUrl,

        @NotNull
        String urlPassword
) {
}
