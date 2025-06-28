package org.noint.pickminbloom.post.dto;

import org.noint.pickminbloom.member.entity.Member;
import org.noint.pickminbloom.post.entity.Post;
import org.noint.pickminbloom.post.enums.PostType;
import org.noint.pickminbloom.post.request.RequestEditPostRequest;
import org.springframework.web.multipart.MultipartFile;

public record RequestEditPostDto(
        Post post,
        String editedName,
        PostType editType,
        MultipartFile editImg,
        Member requester
) {

    public RequestEditPostDto(Post post, RequestEditPostRequest request, Member member) {
        this(
                post,
                request.editName(),
                PostType.fromString(request.editType()),
                request.editPostImg(),
                member
        );
    }
}
