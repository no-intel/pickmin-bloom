package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.post.dto.UpdatePrePostStatusDto;
import org.noint.pickminbloom.post.entity.PrePost;
import org.noint.pickminbloom.post.event.ConfirmPrePostStatus;
import org.noint.pickminbloom.post.event.RejectPrePostStatus;
import org.noint.pickminbloom.post.validator.RejectPrePostValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RejectPrePostService {

    private final ApplicationEventPublisher eventPublisher;
    private final GetPrePostService getPrePostService;
    private final RejectPrePostValidator rejectPrePostValidator;

    public void reject(UpdatePrePostStatusDto dto) {
        log.info("Reject pre post: {}", dto);
        PrePost prePost = getPrePostService.getPrePost(dto.prePostId());
        rejectPrePostValidator.validateStatus(prePost.getStatus());
        prePost.updateStatus(dto.status(), dto.updatedBy());
        eventPublisher.publishEvent(new RejectPrePostStatus(prePost));
    }
}
