package org.noint.pickminbloom.post.validator;

import lombok.RequiredArgsConstructor;
import org.noint.pickminbloom.exception.post.NotWaitingPrePostException;
import org.noint.pickminbloom.post.enums.PrePostStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfirmPrePostValidator {

    public void validateStatus(PrePostStatus status) {
        if (status != PrePostStatus.WAITING) {
            throw new NotWaitingPrePostException(status.toString());
        }
    }
}
