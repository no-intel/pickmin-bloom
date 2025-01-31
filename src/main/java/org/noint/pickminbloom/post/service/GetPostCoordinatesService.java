package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.noint.pickminbloom.post.dto.GetPostCoordinatesDto;
import org.noint.pickminbloom.post.entity.Post;
import org.noint.pickminbloom.post.repository.PostQuerydslRepository;
import org.noint.pickminbloom.post.response.GetPostCoordinatesResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPostCoordinatesService {
    private final PostQuerydslRepository postQuerydslRepository;
    private final GeometryFactory geometryFactory;

    public List<GetPostCoordinatesResponse> getPostCoordinates(GetPostCoordinatesDto dto) {
        Point referencePoint = geometryFactory.createPoint(new Coordinate(dto.getLongitude(), dto.getLatitude()));
        int distance = getDistance(dto.getZoomLevel());
        List<Post> postsWithinDistance = postQuerydslRepository.findPostsWithinDistance(referencePoint, distance);
        return GetPostCoordinatesResponse.create(postsWithinDistance);
    }

    private int getDistance(int zoomLevel) {
        if (zoomLevel < 5 || zoomLevel > 20) {
            throw new IllegalArgumentException("Zoom level must be between 5 and 20");
        }

        return switch (zoomLevel) {
            case 5 -> 500_000;
            case 6, 7, 8, 9, 10 -> 2_000;
            case 11, 12 -> 5_000;
            case 13, 14, 15 -> 1_600;
            case 16, 17, 18 -> 800;
            default -> 400;
        };
    }
}
