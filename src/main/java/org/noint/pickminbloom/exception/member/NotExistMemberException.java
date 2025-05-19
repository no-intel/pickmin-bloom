package org.noint.pickminbloom.exception.member;

import org.noint.pickminbloom.exception.PickminBloomException;

public class NotExistMemberException extends PickminBloomException {
    public NotExistMemberException(String field) {
        super(404, field, "존재 하지 않는 유저");
    }
}
