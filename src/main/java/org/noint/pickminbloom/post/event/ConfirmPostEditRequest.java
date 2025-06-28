package org.noint.pickminbloom.post.event;

import org.noint.pickminbloom.member.entity.Member;
import org.noint.pickminbloom.post.entity.PostEditRequest;
import org.noint.pickminbloom.post.enums.PostType;

public record ConfirmPostEditRequest(
        Long postId,
        Double latitude,
        Double longitude,
        byte[] editImg,
        String editName,
        PostType editType,
        Member requester

) {
    public ConfirmPostEditRequest(PostEditRequest editRequest) {
        this(
                editRequest.getPost().getId(),
                editRequest.getPost().getLatitude(),
                editRequest.getPost().getLongitude(),
                editRequest.getEditImg(),
                editRequest.getEditName(),
                editRequest.getEditType(),
                editRequest.getRequester()
        );
    }
}
