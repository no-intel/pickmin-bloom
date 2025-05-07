package org.noint.pickminbloom.post.service;

import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.exception.post.NotWaitingPrePostException;
import org.noint.pickminbloom.post.dto.UpdatePrePostStatusDto;
import org.noint.pickminbloom.post.entity.PrePost;
import org.noint.pickminbloom.post.enums.PrePostStatus;
import org.noint.pickminbloom.post.event.UpdatePrePostStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RejectPrePostService {

    private final GetPrePostService getPrePostService;

    public void reject(UpdatePrePostStatusDto dto) {
        PrePost prePost = getPrePostService.getPrePost(dto.prePostId());
        if (prePost.getStatus() != PrePostStatus.WAITING) {
            throw new NotWaitingPrePostException(prePost.getStatus().toString());
        }
        prePost.updateStatus(dto.status(), dto.updatedBy());
    }
}
