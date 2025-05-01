package org.noint.pickminbloom.post.dto;

import org.noint.pickminbloom.post.request.GetPostCoordinatesByViewRequest;

public record GetPostCoordinatesByViewDto(
        Double minLongitude,
        Double minLatitude,
        Double maxLongitude,
        Double maxLatitude,
        Double longitude,
        Double latitude
) {

    public GetPostCoordinatesByViewDto(GetPostCoordinatesByViewRequest request) {
        this(
                request.minLongitude(),
                request.minLatitude(),
                request.maxLongitude(),
                request.maxLatitude(),
                request.longitude(),
                request.latitude()
        );
    }
}
