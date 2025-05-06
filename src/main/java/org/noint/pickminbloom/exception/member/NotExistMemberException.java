package org.noint.pickminbloom.exception.member;

import org.noint.pickminbloom.exception.PickminBloomException;

public class NotExistMemberException extends PickminBloomException {
    public NotExistMemberException() {
        super(404, "email", "존재 하지 않는 유저");
    }
}
