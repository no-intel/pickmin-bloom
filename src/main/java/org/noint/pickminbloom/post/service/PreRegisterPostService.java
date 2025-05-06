package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.noint.pickminbloom.post.dto.PreRegisterPostDto;
import org.noint.pickminbloom.post.entity.PrePost;
import org.noint.pickminbloom.post.repository.PrePostRepository;
import org.noint.pickminbloom.post.util.FileCodecUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PreRegisterPostService {

    private final GeometryFactory geometryFactory;
    private final FileCodecUtil fileCodecUtil;
    private final PrePostRepository prePostRepository;

    public void preRegisterPost(PreRegisterPostDto dto) {
        PrePost prePost = new PrePost(
                dto.postName(),
                geometryFactory.createPoint(new Coordinate(dto.postLon(), dto.postLat())),
                dto.type(),
                dto.noImg() ? null : fileCodecUtil.encode(dto.postImg()),
                dto.noImg(),
                dto.registerId()
        );
        prePostRepository.save(prePost);
    }
}
