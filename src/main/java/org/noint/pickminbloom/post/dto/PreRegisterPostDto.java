package org.noint.pickminbloom.post.dto;

import org.noint.pickminbloom.exception.post.NotImgException;
import org.noint.pickminbloom.member.entity.Member;
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
        Member requester
) {
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/jpg",
            "image/webp"
        );

    public PreRegisterPostDto(PreRegisterPostRequest request, Member requester) {
        this(
                request.getPostName(),
                request.getPostLat(),
                request.getPostLon(),
                PostType.fromString(request.getPostType()),
                validateImgFile(request.getPostImg()),
                request.getPostImg() == null,
                requester
        );
    }

    private static MultipartFile validateImgFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
            throw new NotImgException();
        }

        return file;
    }

}
