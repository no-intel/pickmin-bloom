package org.noint.pickminbloom.post.dto;

import org.noint.pickminbloom.post.request.GetPostCoordinatesByViewRequest;

public record GetPostCoordinatesByViewDto(
        Double minX,
        Double minY,
        Double maxX,
        Double maxY) {

    public GetPostCoordinatesByViewDto(GetPostCoordinatesByViewRequest request) {
        this(request.minX(), request.minY(), request.maxX(), request.maxY());
    }
}
