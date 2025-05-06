package org.noint.pickminbloom.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PreRegisterPostRequest {
    @NotBlank(message = "엽서 이름은 필수입니다!")
    private String postName;

    @NotNull(message = "위도를 입력하세요!")
    private Double postLat;

    @NotNull(message = "경도를 입력하세요!")
    private Double postLon;

    private MultipartFile postImg;

    @NotNull(message = "이미지 여부를 명시해야 합니다!")
    private boolean noImg;

    private String postType;

}
