package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.post.dto.RequestEditPostDto;
import org.noint.pickminbloom.post.entity.PostEditRequest;
import org.noint.pickminbloom.post.repository.PostEditRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RequestEditPostService {

    private final PostEditRequestRepository postEditRequestRepository;

    public void requestEditPost(RequestEditPostDto dto) {
        log.info("Request edit post: {}", dto);
        PostEditRequest postEditRequest = new PostEditRequest(
                dto.post(),
                dto.editedName(),
                dto.editLatitude(),
                dto.editLongitude(),
                dto.editImg(),
                dto.editType(),
                dto.requester()
        );
        postEditRequestRepository.save(postEditRequest);
    }
}
