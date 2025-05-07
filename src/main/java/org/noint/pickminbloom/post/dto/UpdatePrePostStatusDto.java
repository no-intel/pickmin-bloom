package org.noint.pickminbloom.post.dto;

import org.noint.pickminbloom.post.enums.PrePostStatus;

public record UpdatePrePostStatusDto(
        Long prePostId,
        PrePostStatus status,
        Long updatedBy
) {
}
