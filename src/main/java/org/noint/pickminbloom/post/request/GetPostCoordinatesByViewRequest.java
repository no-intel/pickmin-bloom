package org.noint.pickminbloom.post.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPostCoordinatesByViewRequest {
    private Double minX;
    private Double minY;
    private Double maxX;
    private Double maxY;
}
