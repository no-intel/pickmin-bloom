package org.noint.pickminbloom.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.noint.pickminbloom.post.request.GetPostCoordinatesByViewRequest;

@Getter
public class GetPostCoordinatesByViewDto {
    private Double minX;
    private Double minY;
    private Double maxX;
    private Double maxY;

    public GetPostCoordinatesByViewDto(GetPostCoordinatesByViewRequest request) {
        this.minX = request.getMinX();
        this.minY = request.getMinY();
        this.maxX = request.getMaxX();
        this.maxY = request.getMaxY();
    }
}
