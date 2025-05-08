package org.noint.pickminbloom.post.event;

import org.noint.pickminbloom.post.entity.PrePost;
import org.noint.pickminbloom.post.enums.PostType;

public record UpdatePrePostStatus(
        Double latitude,
        Double longitude,
        byte[] img,
        String name,
        PostType type,
        Long requesterId,
        Long confirmedBy
) {
    public static UpdatePrePostStatus confirmPrePost(PrePost prePost, Long confirmedBy) {
        return new UpdatePrePostStatus(
                prePost.getLatitude(),
                prePost.getLongitude(),
                prePost.getImg(),
                prePost.getName(),
                prePost.getType(),
                prePost.getRequesterId(),
                confirmedBy
        );
    }
}
