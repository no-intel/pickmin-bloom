package org.noint.pickminbloom.post.dto;

import org.noint.pickminbloom.post.enums.PostType;
import org.noint.pickminbloom.post.request.PreRegisterPostRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public record PreRegisterPostDto(
        String postName,
        Double postLat,
        Double postLon,
        PostType type,
        MultipartFile postImg,
        boolean noImg,
        Long registerId
) {
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/jpg"
        );

    public PreRegisterPostDto(PreRegisterPostRequest request, Long registerId) {
        this(
                request.getPostName(),
                request.getPostLat(),
                request.getPostLon(),
                PostType.fromString(request.getPostType()),
                validateImgFile(request.getPostImg(), request.isNoImg()),
                request.isNoImg(),
                registerId
        );
    }

    private static MultipartFile validateImgFile(MultipartFile file, boolean noImg) {
        if (noImg) {
            return null;
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Only JPEG, PNG and JPG are allowed");
        }

        return file;
    }

}
