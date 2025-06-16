package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noint.pickminbloom.post.dto.UpdatePrePostStatusDto;
import org.noint.pickminbloom.post.entity.PrePost;
import org.noint.pickminbloom.post.enums.PrePostStatus;
import org.noint.pickminbloom.post.event.ConfirmPrePostStatus;
import org.noint.pickminbloom.post.validator.ConfirmPrePostValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ConfirmPrePostService {

    private final ApplicationEventPublisher eventPublisher;
    private final GetPrePostService getPrePostService;
    private final ConfirmPrePostValidator confirmPrePostValidator;

    public void confirm(UpdatePrePostStatusDto dto) {
        log.info("Confirm pre post: {}", dto);
        PrePost prePost = getPrePostService.getPrePost(dto.prePostId());
        confirmPrePostValidator.validateStatus(prePost.getStatus());
        prePost.updateStatus(dto.status(), dto.updatedBy());
        eventPublisher.publishEvent(ConfirmPrePostStatus.confirmPrePost(prePost, dto.updatedBy()));
    }

    // 임시기능
    public void confirmAll() {
        List<PrePost> prePostList = getPrePostService.getAllPrePost();
        for (PrePost prePost : prePostList) {
            prePost.updateStatus(PrePostStatus.CONFIRMED, 1L);
            eventPublisher.publishEvent(ConfirmPrePostStatus.confirmPrePost(prePost, 1L));
        }
    }
}
