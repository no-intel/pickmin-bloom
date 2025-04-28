package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.post.dto.GetPostCoordinatesByViewDto;
import org.noint.pickminbloom.post.entity.Post;
import org.noint.pickminbloom.post.repository.PostQuerydslRepository;
import org.noint.pickminbloom.post.response.GetPostCoordinatesResponse;
import org.noint.pickminbloom.post.util.S3Util;
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
    private final S3Util s3Util;

    public List<GetPostCoordinatesResponse> getPostCoordinates(GetPostCoordinatesByViewDto dto) {
        List<Post> posts = postQuerydslRepository.findPostsByView(dto);
        List<String> geohashes = posts.stream()
                .map(Post::getGeohash)
                .toList();
        Map<String, String> presignedUrls = s3Util.createdPresignedUrlsForDownload(geohashes);
        return GetPostCoordinatesResponse.create(posts, presignedUrls);
    }
}
