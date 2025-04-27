package org.noint.pickminbloom.post.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.noint.pickminbloom.post.dto.RegisterPostDto;
import org.noint.pickminbloom.post.entity.Post;
import org.noint.pickminbloom.post.repository.PostRepository;
import org.noint.pickminbloom.post.request.RegisterPostRequest;
import org.noint.pickminbloom.util.GeoHashUtil;
import org.noint.pickminbloom.util.S3Util;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterPostService {

    private final PostRepository postRepository;
    private final GeoHashUtil geoHashUtil;
    private final GeometryFactory geometryFactory;
    private final S3Util s3Util;

    public void registerPost(RegisterPostRequest request) {
        String geohash = geoHashUtil.encode(request.latitude(), request.longitude());
        RegisterPostDto dto = new RegisterPostDto(geohash, request);
        Post post = new Post(dto.geohash(),
                dto.name(),
                geometryFactory.createPoint(new Coordinate(dto.longitude(), dto.latitude())),
                dto.location(),
                dto.type()
        );
        postRepository.save(post);
    }
}
