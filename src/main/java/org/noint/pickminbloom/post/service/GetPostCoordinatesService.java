package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.post.dto.GetPostCoordinatesByViewDto;
import org.noint.pickminbloom.post.dto.GetPostResponseDto;
import org.noint.pickminbloom.post.repository.PostQuerydslRepository;
import org.noint.pickminbloom.post.response.GetPostCoordinatesResponse;
import org.noint.pickminbloom.post.util.s3.S3util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPostCoordinatesService {
    private final PostQuerydslRepository postQuerydslRepository;
    private final S3util s3Util;

    public List<GetPostCoordinatesResponse> getPostCoordinates(GetPostCoordinatesByViewDto dto) {
        log.info("Get post By coordinates: {}", dto);
        List<GetPostResponseDto> posts = postQuerydslRepository.findPostsByView(dto);
        Map<String, String> downloadUrl = s3Util.getDownloadUrl(posts);
        return GetPostCoordinatesResponse.create(posts, downloadUrl);
    }
}
