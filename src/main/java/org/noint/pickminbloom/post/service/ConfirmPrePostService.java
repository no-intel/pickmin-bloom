package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.post.dto.UpdatePrePostStatusDto;
import org.noint.pickminbloom.post.entity.PrePost;
import org.noint.pickminbloom.post.event.UpdatePrePostStatus;
import org.noint.pickminbloom.post.validator.ConfirmPrePostValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConfirmPrePostService {

    private final ApplicationEventPublisher eventPublisher;
    private final GetPrePostService getPrePostService;
    private final ConfirmPrePostValidator confirmPrePostValidator;

    public void confirm(UpdatePrePostStatusDto dto) {
        PrePost prePost = getPrePostService.getPrePost(dto.prePostId());
        confirmPrePostValidator.validateStatus(prePost.getStatus());
        prePost.updateStatus(dto.status(), dto.updatedBy());
        eventPublisher.publishEvent(UpdatePrePostStatus.confirmPrePost(prePost, dto.updatedBy()));
    }
}
