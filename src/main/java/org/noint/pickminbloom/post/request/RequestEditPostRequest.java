package org.noint.pickminbloom.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record RequestEditPostRequest(
        @NotBlank(message = "엽서 이름은 필수입니다!")
        String editName,

        @NotNull(message = "위도를 입력하세요!")
        Double editLatitude,

        @NotNull(message = "경도를 입력하세요!")
        Double editLongitude,

        @NotBlank(message = "엽서 타입을 선택해주세요!")
        String editType,

        MultipartFile editPostImg
) {
}
