package org.noint.pickminbloom.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPostCoordinatesDto {
    private Double latitude;
    private Double longitude;
    private int zoomLevel;
}
