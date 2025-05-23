package org.noint.pickminbloom.post.dto;

public record UpdatePostEditRequestDto(
        Long postEditRequestId,
        Long confirmedBy
) {
}
