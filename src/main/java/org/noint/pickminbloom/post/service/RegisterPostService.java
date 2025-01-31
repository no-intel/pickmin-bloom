package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.noint.pickminbloom.post.dto.RegisterPostDto;
import org.noint.pickminbloom.post.entity.Post;
import org.noint.pickminbloom.post.repository.PostRepository;
import org.noint.pickminbloom.util.GeoHashUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterPostService {

    private final PostRepository postRepository;
    private final GeoHashUtil geoHashUtil;
    private final GeometryFactory geometryFactory;

    public String registerPost(RegisterPostDto dto) {
        LatLng latLng = convertCoordinates(dto.getCoordinates());
        String geohash = generateGeohash(latLng);
        Point point = createPoint(latLng);
        Post post = new Post(geohash, dto.getName(), point, dto.getLocation(), dto.getType());
        postRepository.save(post);
        return dto.getName();
    }

    private LatLng convertCoordinates(String coordinates) {
        String[] coordinatesArray = coordinates.split(",");
        double latitude = Double.parseDouble(coordinatesArray[0].trim());
        double longitude = Double.parseDouble(coordinatesArray[1].trim());
        return new LatLng(latitude, longitude);
    }

    private String generateGeohash(LatLng latLng) {
        return geoHashUtil.encode(latLng.latitude(), latLng.longitude());
    }

    private Point createPoint(LatLng latLng) {
        return geometryFactory.createPoint(new Coordinate(latLng.longitude(), latLng.latitude()));
    }

    public record LatLng(double latitude, double longitude) {}
}
