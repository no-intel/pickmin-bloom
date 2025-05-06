package org.noint.pickminbloom.post.dto;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public record GetPostResponseDto(
        Long id,
        String geohash,
        String name,
        Double latitude,
        Double longitude,
        String type,
        LocalDateTime createdAt,
        Double distance
) {

    @QueryProjection
    public GetPostResponseDto {}
}

