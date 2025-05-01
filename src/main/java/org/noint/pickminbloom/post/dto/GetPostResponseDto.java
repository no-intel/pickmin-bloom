package org.noint.pickminbloom.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

public record GetPostResponseDto(
        Long id,
        String geohash,
        String name,
        Point coordinates,
        String location,
        String type,
        LocalDateTime createdAt,
        Double distance
) {

    @QueryProjection
    public GetPostResponseDto {}
}

