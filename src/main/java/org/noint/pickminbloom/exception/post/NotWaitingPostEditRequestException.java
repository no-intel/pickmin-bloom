package org.noint.pickminbloom.exception.post;

import org.noint.pickminbloom.exception.PickminBloomException;

public class NotWaitingPostEditRequestException extends PickminBloomException {
    public NotWaitingPostEditRequestException(String message) {
        super(500, "status", "이미 수정 처리된 엽서입니다. Status : " + message);
    }
}
