package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.post.dto.UpdatePostEditRequestDto;
import org.noint.pickminbloom.post.entity.PostEditRequest;
import org.noint.pickminbloom.post.enums.PostEditRequestStatus;
import org.noint.pickminbloom.post.validator.RejectPostEditRequestValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RejectPostEditRequestService {
    private final GetPostEditRequestService getPostEditRequestService;
    private final RejectPostEditRequestValidator rejectPostEditRequestValidator;

    public void reject(UpdatePostEditRequestDto dto) {
        PostEditRequest editPostRequest = getPostEditRequestService.getEditPostRequest(dto.postEditRequestId());
        rejectPostEditRequestValidator.validateStatus(editPostRequest.getEditStatus());
        editPostRequest.confirm(PostEditRequestStatus.REJECTED, dto.confirmedBy());
        log.info("PostEditRequest {} confirmed", editPostRequest.getId());
    }
}
