package org.noint.pickminbloom.exception.post;

import org.noint.pickminbloom.exception.PickminBloomException;

public class NotMatchPostTypeException extends PickminBloomException {
    public NotMatchPostTypeException(String typeKor) {
        super(400, "", typeKor);
    }
}
