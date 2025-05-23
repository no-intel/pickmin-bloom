package org.noint.pickminbloom.post.validator;

import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.exception.post.NotWaitingPostEditRequestException;
import org.noint.pickminbloom.post.enums.PostEditRequestStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfirmPostEditRequestValidator {

    public void validateStatus(PostEditRequestStatus status) {
        if (status != PostEditRequestStatus.WAITING) {
            throw new NotWaitingPostEditRequestException(status.toString());
        }
    }
}
