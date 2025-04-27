package org.noint.pickminbloom.post.request;

public record GetPostCoordinatesByViewRequest(
        Double minX,
        Double minY,
        Double maxX,
        Double maxY
) {
}
