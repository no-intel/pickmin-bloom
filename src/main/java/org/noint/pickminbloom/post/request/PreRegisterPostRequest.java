package org.noint.pickminbloom.post.request;

import lombok.Getter;
import lombok.Setter;
import org.noint.pickminbloom.post.enums.PostType;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PreRegisterPostRequest {
    private String postName;
    private Double postLat;
    private Double postLon;
    private PostType type;
    private MultipartFile postImg;
    private boolean noImg;
}
