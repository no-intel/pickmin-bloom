package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.noint.pickminbloom.post.dto.RegisterPostDto;
import org.noint.pickminbloom.post.entity.Post;
import org.noint.pickminbloom.post.repository.PostRepository;
import org.noint.pickminbloom.post.util.S3Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterPostService {

    private final PostRepository postRepository;
    private final GeometryFactory geometryFactory;
    private final S3Util s3Util;

    public void registerPost(RegisterPostDto dto) {
        Post post = new Post(dto.geohash(),
                dto.name(),
                geometryFactory.createPoint(new Coordinate(dto.longitude(), dto.latitude())),
                dto.location(),
                dto.type()
        );
        postRepository.save(post);
        s3Util.uploadFile(dto.geohash(), dto.image());
    }
}
