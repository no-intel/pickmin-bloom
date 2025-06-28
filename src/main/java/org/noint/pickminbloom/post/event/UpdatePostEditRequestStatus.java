package org.noint.pickminbloom.post.event;

import org.noint.pickminbloom.post.entity.PostEditRequest;
import org.noint.pickminbloom.post.enums.PostType;

public record UpdatePostEditRequestStatus(
        Long postId,
        byte[] editImg,
        String editName,
        PostType editType
) {
    public UpdatePostEditRequestStatus(PostEditRequest editRequest) {
        this(
                editRequest.getPost().getId(),
                editRequest.getEditImg(),
                editRequest.getEditName(),
                editRequest.getEditType()
        );
    }
}
