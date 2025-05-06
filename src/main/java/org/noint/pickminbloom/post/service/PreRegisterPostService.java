package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.exception.post.DuplicateCoordinateException;
import org.noint.pickminbloom.post.dto.PreRegisterPostDto;
import org.noint.pickminbloom.post.entity.Post;
import org.noint.pickminbloom.post.entity.PrePost;
import org.noint.pickminbloom.post.repository.PostQuerydslRepository;
import org.noint.pickminbloom.post.repository.PrePostQuerydslRepository;
import org.noint.pickminbloom.post.repository.PrePostRepository;
import org.noint.pickminbloom.post.util.FileCodecUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PreRegisterPostService {

    private final FileCodecUtil fileCodecUtil;
    private final PrePostRepository prePostRepository;
    private final PrePostQuerydslRepository prePostQuerydslRepository;
    private final PostQuerydslRepository postQuerydslRepository;

    public void preRegisterPost(PreRegisterPostDto dto) {
        Optional<PrePost> prePostData = prePostQuerydslRepository.findByCoor(dto.postLat(), dto.postLon());
        Optional<Post> postData = postQuerydslRepository.findByCoor(dto.postLat(), dto.postLon());
        if (prePostData.isPresent() || postData.isPresent()) {
            throw new DuplicateCoordinateException(dto.postLat() +", " + dto.postLon());
        }

        PrePost prePost = new PrePost(
                dto.postName(),
                dto.postLat(),
                dto.postLon(),
                dto.type(),
                dto.noImg() ? null : fileCodecUtil.encode(dto.postImg()),
                dto.noImg(),
                dto.registerId()
        );
        prePostRepository.save(prePost);
    }
}
