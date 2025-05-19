package org.noint.pickminbloom.post.dto;

import org.noint.pickminbloom.member.entity.Member;
import org.noint.pickminbloom.post.entity.Post;
import org.noint.pickminbloom.post.enums.PostType;
import org.noint.pickminbloom.post.request.RequestEditPostRequest;
import org.springframework.web.multipart.MultipartFile;

public record RequestEditPostDto(
        Post post,
        String editedName,
        Double editLatitude,
        Double editLongitude,
        PostType editType,
        MultipartFile editImg,
        Member requester
) {

    public RequestEditPostDto(Post post, RequestEditPostRequest request, Member member) {
        this(
                post,
                request.editName(),
                request.editLatitude(),
                request.editLongitude(),
                PostType.fromString(request.editType()),
                request.editPostImg(),
                member
        );
    }
}
