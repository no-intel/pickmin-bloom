package org.noint.pickminbloom.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import org.noint.pickminbloom.post.enums.PostType;

import java.time.LocalDateTime;

public record GetPostResponseDto(
        Long id,
        String geohash,
        String name,
        Double latitude,
        Double longitude,
        PostType type,
        boolean noImg,
        LocalDateTime createdAt,
        Double distance
) {

    @QueryProjection
    public GetPostResponseDto {}

    public String getKey() {
        return type + "-" + geohash;
    }
}

