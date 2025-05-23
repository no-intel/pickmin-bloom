package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.post.dto.UpdatePostEditRequestDto;
import org.noint.pickminbloom.post.entity.PostEditRequest;
import org.noint.pickminbloom.post.enums.PostEditRequestStatus;
import org.noint.pickminbloom.post.event.UpdatePostEditRequestStatus;
import org.noint.pickminbloom.post.validator.ConfirmPostEditRequestValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ConfirmPostEditRequestService {
    private final ApplicationEventPublisher eventPublisher;
    private final GetPostEditRequestService getPostEditRequestService;
    private final ConfirmPostEditRequestValidator confirmPostEditRequestValidator;

    public void confirm(UpdatePostEditRequestDto dto) {
        PostEditRequest editPostRequest = getPostEditRequestService.getEditPostRequest(dto.postEditRequestId());
        confirmPostEditRequestValidator.validateStatus(editPostRequest.getEditStatus());
        editPostRequest.confirm(PostEditRequestStatus.CONFIRMED, dto.confirmedBy());
        log.info("PostEditRequest {} confirmed", editPostRequest.getId());
        eventPublisher.publishEvent(new UpdatePostEditRequestStatus(editPostRequest));
    }
}
