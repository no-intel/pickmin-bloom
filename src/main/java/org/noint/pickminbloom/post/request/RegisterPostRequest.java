package org.noint.pickminbloom.post.request;

import org.springframework.web.multipart.MultipartFile;

public record RegisterPostRequest(
        Double latitude,
        Double longitude,
        MultipartFile image,
        String name,
        String type
) {
}
