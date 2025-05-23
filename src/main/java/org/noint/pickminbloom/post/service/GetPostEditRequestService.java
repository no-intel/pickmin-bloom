package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.exception.post.NotExistPostEditRequestException;
import org.noint.pickminbloom.post.entity.PostEditRequest;
import org.noint.pickminbloom.post.repository.PostEditRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPostEditRequestService {
    private final PostEditRequestRepository postEditRequestRepository;

    public PostEditRequest getEditPostRequest(Long editPostRequestId) {
        return postEditRequestRepository.findById(editPostRequestId)
                .orElseThrow(() -> new NotExistPostEditRequestException("id"));
    }
}
