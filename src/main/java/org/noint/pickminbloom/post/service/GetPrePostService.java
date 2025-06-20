package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.exception.post.NotExistPrePostException;
import org.noint.pickminbloom.post.entity.PrePost;
import org.noint.pickminbloom.post.repository.PrePostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPrePostService {
    private final PrePostRepository prePostRepository;

    public PrePost getPrePost(Long prePostId) {
        log.info("getPrePost: {}", prePostId);
        return prePostRepository.findById(prePostId)
                .orElseThrow(() -> new NotExistPrePostException("id"));
    }

    // 임시기능
    public List<PrePost> getAllPrePost() {
        return prePostRepository.findAll();
    }
}
