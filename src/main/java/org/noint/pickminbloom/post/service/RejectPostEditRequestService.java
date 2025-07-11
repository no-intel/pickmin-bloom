package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.post.dto.UpdatePostEditRequestDto;
import org.noint.pickminbloom.post.entity.PostEditRequest;
import org.noint.pickminbloom.post.enums.PostEditRequestStatus;
import org.noint.pickminbloom.post.event.RejectPostEditRequest;
import org.noint.pickminbloom.post.validator.RejectPostEditRequestValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RejectPostEditRequestService {
    private final GetPostEditRequestService getPostEditRequestService;
    private final RejectPostEditRequestValidator rejectPostEditRequestValidator;
    private final ApplicationEventPublisher eventPublisher;

    public void reject(UpdatePostEditRequestDto dto) {
        log.info("Reject post edit request: {}", dto);
        PostEditRequest editPostRequest = getPostEditRequestService.getEditPostRequest(dto.postEditRequestId());
        rejectPostEditRequestValidator.validateStatus(editPostRequest.getEditStatus());
        editPostRequest.confirm(PostEditRequestStatus.REJECTED, dto.confirmedBy());
        log.info("PostEditRequest {} confirmed", editPostRequest.getId());
        eventPublisher.publishEvent(new RejectPostEditRequest(editPostRequest));
    }
}
