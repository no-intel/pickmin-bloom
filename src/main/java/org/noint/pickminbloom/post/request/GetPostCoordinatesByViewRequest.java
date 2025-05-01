package org.noint.pickminbloom.post.request;

public record GetPostCoordinatesByViewRequest(
        Double minLongitude,
        Double minLatitude,
        Double maxLongitude,
        Double maxLatitude,
        Double longitude,
        Double latitude
) {
}
