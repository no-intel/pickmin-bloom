package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.exception.post.NotExistPostException;
import org.noint.pickminbloom.post.entity.Post;
import org.noint.pickminbloom.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPostService {
    private final PostRepository postRepository;

    public Post getPost(Long postId) {
        log.info("getPost: {}", postId);
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotExistPostException("id"));
    }
}
