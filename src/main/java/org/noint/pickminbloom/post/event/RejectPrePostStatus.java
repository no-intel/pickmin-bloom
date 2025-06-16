package org.noint.pickminbloom.post.event;

import org.noint.pickminbloom.member.entity.Member;
import org.noint.pickminbloom.post.entity.PrePost;
import org.noint.pickminbloom.post.enums.PostType;

public record RejectPrePostStatus(
        Double latitude,
        Double longitude,
        byte[] img,
        String name,
        PostType type,
        Member requester,
        boolean noImg
) {
    public RejectPrePostStatus (PrePost prePost) {
        this(
                prePost.getLatitude(),
                prePost.getLongitude(),
                prePost.getImg(),
                prePost.getName(),
                prePost.getType(),
                prePost.getRequester(),
                prePost.isNoImg()
        );
    }
}
