package org.noint.pickminbloom.exception.post;

import org.noint.pickminbloom.exception.PickminBloomException;

public class DuplicateCoordinateException extends PickminBloomException {
    public DuplicateCoordinateException(String message) {
        super(500, "", "이미 등록 됐거나 신청된 좌표입니다. " + message);
    }
}
