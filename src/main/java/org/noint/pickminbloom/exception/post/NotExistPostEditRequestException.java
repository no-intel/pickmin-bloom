package org.noint.pickminbloom.exception.post;

import org.noint.pickminbloom.exception.PickminBloomException;

public class NotExistPostEditRequestException extends PickminBloomException {
    public NotExistPostEditRequestException(String field) {
        super(404, field, "존재 하지 않는 엽서 수정 신청");
    }
}
