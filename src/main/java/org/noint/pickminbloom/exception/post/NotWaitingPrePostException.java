package org.noint.pickminbloom.exception.post;

import org.noint.pickminbloom.exception.PickminBloomException;

public class NotWaitingPrePostException extends PickminBloomException {
    public NotWaitingPrePostException(String message) {
        super(500, "status", "이미 처리된 엽서입니다. Status : " + message);
    }
}
