package org.noint.pickminbloom.post.event;

import org.noint.pickminbloom.member.entity.Member;
import org.noint.pickminbloom.post.entity.Post;
import org.noint.pickminbloom.post.entity.PostEditRequest;
import org.noint.pickminbloom.post.entity.PrePost;
import org.noint.pickminbloom.post.enums.PostType;

public record RegisterEditPost(
        Post post,
        Long editId,
        byte[] editImg,
        String editName,
        PostType editType,
        Member requester
) {
    public RegisterEditPost(PostEditRequest editRequest) {
        this(
                editRequest.getPost(),
                editRequest.getId(),
                editRequest.getEditImg(),
                editRequest.getEditName(),
                editRequest.getEditType(),
                editRequest.getRequester()
        );
    }
}
