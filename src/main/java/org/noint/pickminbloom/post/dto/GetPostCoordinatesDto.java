package org.noint.pickminbloom.post.dto;

public record GetPostCoordinatesDto(
        Double latitude,
        Double longitude,
        int zoomLevel
) {
}
