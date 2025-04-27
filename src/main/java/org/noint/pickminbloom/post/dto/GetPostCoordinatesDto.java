package org.noint.pickminbloom.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class GetPostCoordinatesDto {
    private Double latitude; // 위도
    private Double longitude; // 경도
    private int zoomLevel;
}
